package com.social.network.users.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.util.IOUtils;
import com.social.network.users.dao.HardSkillRepository;
import com.social.network.users.dao.UserAvatarS3Repository;
import com.social.network.users.dao.UserRepository;
import com.social.network.users.dto.entry.UserEntry;
import com.social.network.users.entity.Country;
import com.social.network.users.entity.HardSkill;
import com.social.network.users.entity.User;
import com.social.network.users.exceptions.NotFoundException;
import com.social.network.users.util.Utils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
public class UserService {
    private static final String PHOTO_LINK_PREFIX = "/minio/post-photo-bucket/";
    private static final String USER_NOT_FOUND = "User not found";

    private final UserRepository userRepository;
    private final HardSkillRepository hardSkillService;
    private final CountryService countryService;
    private final UserAvatarS3Repository avatarS3Repository;

    @PersistenceContext
    private EntityManager entityManager;

    public UserService(UserRepository userRepository,
                       HardSkillRepository hardSkillService,
                       CountryService countryService,
                       UserAvatarS3Repository avatarS3Repository) {
        this.userRepository = userRepository;
        this.hardSkillService = hardSkillService;
        this.countryService = countryService;
        this.avatarS3Repository = avatarS3Repository;
    }

    public Page<UserEntry> getPageUsers(int pageIndex, int pageSize, String countryName, Sort sort) {
        log.info(String.format("Get users page: %s, size: %s", pageIndex, pageSize));
        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);

        Page<User> users;
        if (Strings.isNotBlank(countryName)) {
            users = userRepository.findAllByCountry_Name(pageable, countryName);
            return new PageImpl<>(UserEntry.fromListUsers(users.getContent()), pageable, users.getTotalElements());
        }

        users = userRepository.findAll(pageable);
        return new PageImpl<>(UserEntry.fromListUsers(users.getContent()), pageable, users.getTotalElements());
    }

    public Page<UserEntry> getFollowersByUserId(int pageIndex, int pageSize, UUID userId, Sort sort) {
        log.info(String.format("Get page followers by userId[%s] page: %s, size: %s", userId, pageIndex, pageSize));
        Utils.required(userId, "User id is required");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, USER_NOT_FOUND));

        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
        Page<User> users = userRepository.getFollowers(user.getId(), pageable);
        return new PageImpl<>(UserEntry.fromListUsers(users.getContent()), pageable, users.getTotalElements());
    }

    public List<UserEntry> getAllUsers(boolean isDeleted) {
        log.info(String.format("Get all users page isDeleted[%s]", isDeleted));

        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedProductFilter");
        filter.setParameter("isDeleted", isDeleted);
        List<User> users = userRepository.findAll();
        session.disableFilter("deletedProductFilter");
        return UserEntry.fromListUsers(users);
    }

    public UserEntry getUserById(UUID userId) {
        log.info(String.format("Get user by id[%s]", userId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, USER_NOT_FOUND));
        return UserEntry.fromUser(user);
    }

    public UUID createUser(User user, List<String> hardSkillsInput) {
        Utils.required(user, "User is required");
        Utils.verifyEmail(user.getEmail(), "Email is not valid");

        setCountry(user.getCountry(), user);
        setHardSkills(hardSkillsInput, user);
        User savedUser = userRepository.save(user);
        log.info("Saved user");
        return savedUser.getId();
    }

    public UUID updateUser(UUID id, User user, List<String> hardSkillsInput) {
        log.info(String.format("Updating user by id[%s]...", id));
        Utils.required(id, "User id is required");
        Utils.required(user, "User is required");
        Utils.state(!userRepository.existsById(id), "User doesn't exist");

        setCountry(user.getCountry(), user);
        setHardSkills(hardSkillsInput, user);
        User savedUser = userRepository.save(user);
        log.info(String.format("Updated user by id[%s]", id));
        return savedUser.getId();
    }

    public void deleteUser(UUID id) {
        log.info(String.format("Deleting user by id[%s]...", id));
        userRepository.deleteById(id);
        log.info(String.format("Deleted user by id[%s]...", id));
    }

    public void subscribe(UUID userId, UUID followerId) {
        log.info(String.format("user[%s] subscribe on user[%s]", followerId, userId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, USER_NOT_FOUND));
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, USER_NOT_FOUND));
        user.getFollowers().add(follower);
        userRepository.save(user);
    }

    public void unsubscribe(UUID userId, UUID followerId) {
        log.info(String.format("user[%s] unsubscribe on user[%s]", followerId, userId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, USER_NOT_FOUND));
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, USER_NOT_FOUND));
        user.getFollowers().remove(follower);
        userRepository.save(user);
    }

    private void setHardSkills(List<String> hardSkillsInput, User user) {
        if (hardSkillsInput == null || hardSkillsInput.isEmpty())
            return;

        List<HardSkill> hardSkills = hardSkillsInput.stream()
                .map(skill -> {
                    HardSkill hardSkill = hardSkillService.findByName(skill);
                    if (hardSkill == null) {
                        HardSkill hs = new HardSkill(skill);
                        hardSkill = hardSkillService.save(hs);
                    }
                    return hardSkill;
                }).toList();

        user.setHardSkills(hardSkills);
    }

    private void setCountry(Country country, User user) {
        if (country == null)
            return;

        country = countryService.getCountryById(country.getId());
        user.setCountry(country);
    }

    public String saveAndGetAvatar(String userId, MultipartFile file) {
        log.info("Create avatar...");
        Utils.required(userId, "User id is required");
        Utils.required(file, "File is required");

        String avatarUrl;
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new IllegalStateException("User not found"));
        try {
            String filename = file.getOriginalFilename();
            Path tempFile = Files.createTempFile("temp",
                    "." + FilenameUtils.getExtension(filename));

            File saveTempFile = tempFile.toFile();
            try (InputStream fileInputStream = file.getInputStream();
                 FileOutputStream output = new FileOutputStream(saveTempFile);
                 FileInputStream inputStream = new FileInputStream(saveTempFile)) {
                IOUtils.copy(fileInputStream, output);
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(saveTempFile.length());
                metadata.setContentType(Files.probeContentType(Paths.get(saveTempFile.toURI())));
                avatarS3Repository.put(filename, inputStream, metadata);
            }

            avatarUrl = PHOTO_LINK_PREFIX + filename;
            user.setAvatarUrl(avatarUrl);
            userRepository.save(user);
        } catch (IOException exception) {
            throw new IllegalStateException("Something happen while save avatar for user: " + userId, exception);
        }

        log.info("Created avatar for userId:{}", userId);
        return avatarUrl;
    }
}
