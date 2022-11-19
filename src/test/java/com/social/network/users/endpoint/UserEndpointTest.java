package com.social.network.users.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.social.network.users.dao.UserRepository;
import com.social.network.users.entity.User;
import com.social.network.users.entity.dto.UserInput;
import com.social.network.users.service.UserService;
import com.social.network.users.util.Utils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.social.network.users.entity.UserSex.MALE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-test.yaml")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserEndpointTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    private void init() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void create_test_200() throws Exception {
        UserInput userInput = createUserInput();

        mvc.perform(post("/v1/users")
                        .content(objectMapper.writeValueAsString(userInput))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.response").isString());
    }

    @Test
    void getAllUsers_200() throws Exception {
        User user = Utils.createUser(createUserInput());
        userService.createUser(user, Collections.emptyList());

        mvc.perform(get("/v1/users/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").isArray())
                .andExpect(jsonPath("$.response", Matchers.hasSize(1)));
    }

    @Test
    void user_followers_test_200() throws Exception {
        User follower = User.builder()
                .name("name2")
                .surname("surname2")
                .nickname("nickname2")
                .sex(MALE)
                .birthdate(new Date())
                .email("usernam2e@gmail.com")
                .build();
        follower = userRepository.save(follower);

        User user = User.builder()
                .name("name1")
                .surname("surname")
                .nickname("nickname")
                .sex(MALE)
                .birthdate(new Date())
                .email("username@gmail.com")
                .followers(List.of(follower))
                .build();
        user = userRepository.save(user);

        mvc.perform(get("/v1/users/" + user.getId() + "/followers")
                        .param("pageIndex", "0")
                        .param("pageSize", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response.content[0].id").value(follower.getId().toString()));
    }

    private UserInput createUserInput() {
        UserInput userInput = new UserInput();
        userInput.setId(UUID.randomUUID().toString());
        userInput.setName("test_name");
        userInput.setSurname("test_surname");
        userInput.setSex("MALE");
        userInput.setBirthdate(new Date());
        userInput.setAvatar("http://");
        userInput.setUserDescription("description");
        userInput.setNickname(UUID.randomUUID().toString());
        userInput.setEmail("username@gmail.com");
        userInput.setPhoneNumber("+79991919093");
        userInput.setHardSkills(Arrays.asList("Java", "Spring"));
        userInput.setCountry(1L);
        return userInput;
    }

}
