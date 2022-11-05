package com.social.network.users.service;

import com.social.network.users.dao.HardSkillRepository;
import com.social.network.users.dao.UserRepository;
import com.social.network.users.entity.Country;
import com.social.network.users.entity.HardSkill;
import com.social.network.users.entity.User;
import com.social.network.users.entity.dto.UserEntry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

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
import static org.mockito.Mockito.when;

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

    @Test
    void getPageUsers_testPassed() {
        int pageIndex = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageIndex, pageSize);

        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());

        when(userRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(users, pageable, users.size()));

        Page<UserEntry> userEntries = userService.getPageUsers(pageIndex, pageSize, null);
        assertEquals(2L, userEntries.getTotalElements());
    }

    @Test
    void getAllUsers_testPassed() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());

        when(userRepository.findAll()).thenReturn(users);

        List<UserEntry> userEntries = userService.getAllUsers(false);
        assertEquals(2L, userEntries.size());
    }

    @Test
    void getUserById_testPassed() {
        UUID uuid = UUID.randomUUID();
        User user = new User();
        user.setId(uuid);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        UserEntry userEntry = userService.getUserById(uuid);
        assertEquals(uuid.toString(), userEntry.getId());
    }

    @Test
    void getUserById_testException() {
        UUID uuid = UUID.randomUUID();
        User user = new User();
        user.setId(uuid);
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> userService.getUserById(uuid));
    }

    @Test
    void createUser_testException() {
        User user = createMockUser();
        user.setEmail("asdasd");

        List<String> hardSkillsInput = Collections.emptyList();
        assertThrows(IllegalArgumentException.class,
                () -> userService.createUser(null, hardSkillsInput));
        assertThrows(IllegalArgumentException.class,
                () -> userService.createUser(user, hardSkillsInput));
    }

    @Test
    void createUser_testPassed() {
        HardSkill hardSkill = new HardSkill(JAVA_SKILL);
        HardSkill hardSkill1 = new HardSkill(SPRING_SKILL);

        User user = createMockUser();
        user.setHardSkills(Arrays.asList(hardSkill, hardSkill1));
        List<String> hardSkillsInput = createMockHardSkills();

        when(userRepository.save(any())).thenReturn(user);
        when(hardSkillService.findByName(JAVA_SKILL)).thenReturn(hardSkill);
        when(hardSkillService.findByName(SPRING_SKILL)).thenReturn(null);
        when(hardSkillService.save(any())).thenReturn(hardSkill1);

        assertEquals(user.getId(), userService.createUser(user, hardSkillsInput));
    }

    @Test
    void updateUser_testException() {
        User user = createMockUser();
        user.setEmail("asdasd");

        UUID uuid = UUID.randomUUID();
        List<String> hardSkillsInput = Collections.emptyList();

        when(userRepository.existsById(any())).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> userService.updateUser(uuid, null, hardSkillsInput));
        assertThrows(IllegalArgumentException.class,
                () -> userService.updateUser(null, user, hardSkillsInput));
        assertThrows(IllegalArgumentException.class,
                () -> userService.updateUser(uuid, user, hardSkillsInput));
    }

    @Test
    void updateUser_testPassed() {
        HardSkill hardSkill = new HardSkill(JAVA_SKILL);
        HardSkill hardSkill1 = new HardSkill(SPRING_SKILL);

        UUID uuid = UUID.randomUUID();
        User user = createMockUser();
        user.setHardSkills(Arrays.asList(hardSkill, hardSkill1));
        List<String> hardSkillsInput = createMockHardSkills();

        when(userRepository.existsById(any())).thenReturn(true);
        when(userRepository.save(any())).thenReturn(user);
        when(hardSkillService.findByName(JAVA_SKILL)).thenReturn(hardSkill);
        when(hardSkillService.findByName(SPRING_SKILL)).thenReturn(null);
        when(hardSkillService.save(any())).thenReturn(hardSkill1);

        assertEquals(user.getId(), userService.updateUser(uuid, user, hardSkillsInput));
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
