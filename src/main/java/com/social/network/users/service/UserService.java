package com.social.network.users.service;

import com.social.network.users.dao.HardSkillRepository;
import com.social.network.users.dao.UserRepository;
import com.social.network.users.entity.Country;
import com.social.network.users.entity.HardSkill;
import com.social.network.users.entity.User;
import com.social.network.users.entity.dto.UserEntry;
import com.social.network.users.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public record UserService(UserRepository userRepository,
                          HardSkillRepository hardSkillService,
                          CountryService countryService) {

    public Page<UserEntry> getPageUsers(int pageIndex, int pageSize, String countryName) {
        log.info(String.format("Get page users page: %s, size: %s", pageIndex, pageSize));
        Pageable pageable = PageRequest.of(pageIndex, pageSize);

        Page<User> users;
        if (Strings.isNotBlank(countryName)) {
            users = userRepository.findAllByCountry_Name(pageable, countryName);
            return new PageImpl<>(UserEntry.fromListUsers(users.getContent()), pageable, users.getTotalElements());
        }

        users = userRepository.findAll(pageable);
        return new PageImpl<>(UserEntry.fromListUsers(users.getContent()), pageable, users.getTotalElements());
    }

    public List<UserEntry> getAllUsers() {
        log.info("Get all users page");
        return UserEntry.fromListUsers(userRepository.findAll());
    }

    public UserEntry getUserById(UUID userId) {
        log.info(String.format("Get user by id[%s]", userId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        user.getFollowers().add(follower);
        userRepository.save(user);
    }

    public void unsubscribe(UUID userId, UUID followerId) {
        log.info(String.format("user[%s] unsubscribe on user[%s]", followerId, userId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
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
                        HardSkill hardSkill1 = new HardSkill(skill);
                        hardSkill = hardSkillService.save(hardSkill1);
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

}
