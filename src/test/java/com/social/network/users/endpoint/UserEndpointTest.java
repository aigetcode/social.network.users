package com.social.network.users.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.social.network.users.entity.User;
import com.social.network.users.entity.dto.UserInput;
import com.social.network.users.service.UserService;
import com.social.network.users.util.Utils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserEndpointTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Test
    @Sql("classpath:testdata.sql")
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
    @Sql("classpath:testdata.sql")
    void getAllUsers_200() throws Exception {
        User user = Utils.createUser(createUserInput());
        userService.createUser(user, Collections.emptyList());

        mvc.perform(get("/v1/users/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").isArray())
                .andExpect(jsonPath("$.response", Matchers.hasSize(1)));
    }

    private UserInput createUserInput() {
        UserInput userInput = new UserInput();
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
