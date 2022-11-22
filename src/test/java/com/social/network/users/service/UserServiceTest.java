package com.social.network.users.service;

import com.social.network.users.dao.HardSkillRepository;
import com.social.network.users.dao.UserRepository;
import com.social.network.users.entity.Country;
import com.social.network.users.entity.HardSkill;
import com.social.network.users.entity.User;
import com.social.network.users.entity.dto.UserEntry;
import com.social.network.users.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private final String JAVA_SKILL = "Java";
    private final String SPRING_SKILL = "Spring";

    @Mock
    private UserRepository userRepository;

    @Mock
    private HardSkillRepository hardSkillService;

    @Mock
    private CountryService countryService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void init() {
        Country country = new Country("123", "123", "123");
        lenient().doReturn(country).when(countryService).getCountryById(any());
    }

    @Test
    void getPageUsers_passed() {
        int pageIndex = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageIndex, pageSize,
                Sort.by("name").and(Sort.by("surname")));
        List<User> users = Arrays.asList(new User(), new User());

        doReturn(new PageImpl<>(users, pageable, users.size()))
                .when(userRepository).findAll((Pageable) any());

        Page<UserEntry> userEntries = userService.getPageUsers(pageIndex, pageSize,
                null, Sort.by("name").and(Sort.by("surname")));
        assertEquals(2L, userEntries.getTotalElements());
    }

    @Test
    void getPageUsersWithCountry_passed() {
        int pageIndex = 0;
        int pageSize = 10;
        String country = "123";
        Pageable pageable = PageRequest.of(pageIndex, pageSize,
                Sort.by("name").and(Sort.by("surname")));
        List<User> users = Arrays.asList(new User(), new User());

        doReturn(new PageImpl<>(users, pageable, users.size()))
                .when(userRepository).findAllByCountry_Name(any(), eq(country));

        Page<UserEntry> userEntries = userService.getPageUsers(pageIndex, pageSize,
                country, Sort.by("name").and(Sort.by("surname")));
        assertEquals(2L, userEntries.getTotalElements());
    }

    @Test
    void getAllUsers_passed() {
        List<User> users = Arrays.asList(new User(), new User());

        doReturn(users).when(userRepository).findAll();

        List<User> userEntries = userRepository.findAll();
        assertEquals(users, userEntries);
    }

    @Test
    void getUserById_passed() {
        UUID uuid = UUID.randomUUID();
        User user = new User();
        user.setId(uuid);

        doReturn(Optional.of(user)).when(userRepository).findById(any());

        UserEntry userEntry = userService.getUserById(uuid);
        assertEquals(uuid.toString(), userEntry.getId());
    }

    @Test
    void getUserById_exception() {
        UUID uuid = UUID.randomUUID();
        User user = new User();
        user.setId(uuid);

        doReturn(Optional.empty()).when(userRepository).findById(any());

        assertThrows(NotFoundException.class,
                () -> userService.getUserById(uuid));
    }

    @Test
    void createUser_exception() {
        User user = createMockUser();
        user.setEmail("asdasd");

        List<String> hardSkillsInput = Collections.emptyList();
        assertThrows(IllegalArgumentException.class,
                () -> userService.createUser(null, hardSkillsInput));
        assertThrows(IllegalArgumentException.class,
                () -> userService.createUser(user, hardSkillsInput));
    }

    @Test
    void createUser_passed() {
        HardSkill hardSkill = new HardSkill(JAVA_SKILL);
        HardSkill hardSkill1 = new HardSkill(SPRING_SKILL);

        User user = createMockUser();
        user.setHardSkills(Arrays.asList(hardSkill, hardSkill1));
        List<String> hardSkillsInput = createMockHardSkills();

        doReturn(user).when(userRepository).save(any());
        doReturn(hardSkill).when(hardSkillService).findByName(JAVA_SKILL);
        doReturn(null).when(hardSkillService).findByName(SPRING_SKILL);
        doReturn(null).when(hardSkillService).save(any());

        assertEquals(user.getId(), userService.createUser(user, hardSkillsInput));
    }

    @Test
    void updateUser_exception() {
        User user = createMockUser();
        user.setEmail("asdasd");

        UUID uuid = UUID.randomUUID();
        List<String> hardSkillsInput = Collections.emptyList();

        doReturn(false).when(userRepository).existsById(any());

        assertThrows(IllegalArgumentException.class,
                () -> userService.updateUser(uuid, null, hardSkillsInput));
        assertThrows(IllegalArgumentException.class,
                () -> userService.updateUser(null, user, hardSkillsInput));
        assertThrows(IllegalArgumentException.class,
                () -> userService.updateUser(uuid, user, hardSkillsInput));
    }

    @Test
    void updateUser_passed() {
        HardSkill hardSkill = new HardSkill(JAVA_SKILL);
        HardSkill hardSkill1 = new HardSkill(SPRING_SKILL);

        UUID uuid = UUID.randomUUID();
        User user = createMockUser();
        user.setHardSkills(Arrays.asList(hardSkill, hardSkill1));
        List<String> hardSkillsInput = createMockHardSkills();

        doReturn(true).when(userRepository).existsById(any());
        doReturn(user).when(userRepository).save(any());
        doReturn(hardSkill).when(hardSkillService).findByName(JAVA_SKILL);
        doReturn(null).when(hardSkillService).findByName(SPRING_SKILL);
        doReturn(hardSkill1).when(hardSkillService).save(any());

        assertEquals(user.getId(), userService.updateUser(uuid, user, hardSkillsInput));
    }

    @Test
    void getFollowersByUserId_exception() {
        int pageIndex = 0;
        int pageSize = 10;
        Sort sort = Sort.by("name").and(Sort.by("surname"));
        UUID uuid = UUID.randomUUID();

        assertThrows(NotFoundException.class,
                () -> userService.getFollowersByUserId(pageIndex, pageSize, uuid, sort)
        );
    }

    @Test
    void subscribe_passed() {
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();

        User user1 = new User();
        user1.setId(userId1);
        User user2 = new User();
        user2.setId(userId2);

        doReturn(Optional.of(user1)).when(userRepository).findById(userId1);
        doReturn(Optional.of(user2)).when(userRepository).findById(userId2);
        doReturn(user1).when(userRepository).save(any());

        userService.unsubscribe(userId1, userId2);
        verify(userRepository).save(any());
    }

    @Test
    void unsubscribe_passed() {
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();

        User user1 = new User();
        user1.setId(userId1);
        User user2 = new User();
        user2.setId(userId2);

        doReturn(Optional.of(user1)).when(userRepository).findById(userId1);
        doReturn(Optional.of(user2)).when(userRepository).findById(userId2);
        doReturn(user1).when(userRepository).save(any());

        userService.subscribe(userId1, userId2);
        verify(userRepository).save(any());
    }

    private User createMockUser() {
        User user = new User();
        user.setName("test");
        user.setSurname("testSurname");
        user.setEmail("username@gmail.com");

        Country country = createMockCountry();
        user.setCountry(country);

        return user;
    }

    private Country createMockCountry() {
        Country country = new Country();
        country.setId(new Random().nextLong());
        country.setName(UUID.randomUUID().toString());
        return country;
    }

    private List<String> createMockHardSkills() {
        List<String> hardSkills = new ArrayList<>();
        hardSkills.add(JAVA_SKILL);
        hardSkills.add(SPRING_SKILL);
        return hardSkills;
    }

}
