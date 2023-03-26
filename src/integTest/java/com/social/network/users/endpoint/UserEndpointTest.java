package com.social.network.users.endpoint;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.social.network.users.dto.entry.UserEntry;
import com.social.network.users.dto.input.UserInput;
import com.social.network.users.endpoint.mvc.SuccessResponse;
import com.social.network.users.containers.PostgresContainerWrapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StreamUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers(disabledWithoutDocker = true)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@ActiveProfiles("integTest")
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-integTest.yaml")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserEndpointTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AmazonS3 client;

    @Container
    private static final PostgreSQLContainer<PostgresContainerWrapper> postgresContainer = new PostgresContainerWrapper();

    @DynamicPropertySource
    public static void initSystemParams(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Test
    @Sql(scripts = "classpath:./sql/drop-users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            config = @SqlConfig(encoding = "utf-8"))
    void create_test_200() throws Exception {
        UserInput userInput = createUserInput();

        mvc.perform(post("/v1/users")
                        .content(objectMapper.writeValueAsString(userInput))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").isString());
    }

    @Test
    @Sql(value = "classpath:./sql/insert-user.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(encoding = "utf-8"))
    @Sql(value = "classpath:./sql/drop-users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            config = @SqlConfig(encoding = "utf-8"))
    void getAllUsers_200() throws Exception {
        mvc.perform(get("/v1/users/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").isArray())
                .andExpect(jsonPath("$.response", Matchers.hasSize(4)));
    }

    @Test
    @Sql(value = {"classpath:./sql/insert-user.sql", "classpath:./sql/insert-followers.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(encoding = "utf-8"))
    @Sql(value = "classpath:./sql/drop-users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            config = @SqlConfig(encoding = "utf-8"))
    void user_followers_test_200() throws Exception {
        String uuid = "68cdef45-e98e-4e86-91c8-b8fe437ae01f";
        String expectedUuid = "69cdef45-e98e-4e86-91c8-b8fe437ae01f";

        mvc.perform(get("/v1/users/" + uuid + "/followers")
                        .param("pageIndex", "0")
                        .param("pageSize", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response.content[0].id").value(expectedUuid));
    }

    @Test
    @Sql(value = "classpath:./sql/drop-users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            config = @SqlConfig(encoding = "utf-8"))
    void user_followers_test_404() throws Exception {
        String uuid = UUID.randomUUID().toString();

        mvc.perform(get("/v1/users/" + uuid + "/followers")
                        .param("pageIndex", "0")
                        .param("pageSize", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @Sql(value = "classpath:./sql/insert-user.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(encoding = "utf-8"))
    @Sql(value = "classpath:./sql/drop-users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            config = @SqlConfig(encoding = "utf-8"))
    void user_by_id_test_200() throws Exception {
        String uuid = "68cdef45-e98e-4e86-91c8-b8fe437ae01f";
        UserEntry expectedUserEntry = UserEntry.builder()
                .id(uuid)
                .version(0)
                .name("testName")
                .surname("testSurname")
                .nickname("nickname")
                .sex("MALE")
                .hardSkills(Collections.emptyList())
                .build();
        SuccessResponse<UserEntry> expectedSuccessResponse = new SuccessResponse<>(true, expectedUserEntry);

        mvc.perform(get("/v1/users/" + uuid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedSuccessResponse)));
    }

    @Test
    @Sql(value = "classpath:./sql/insert-user.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(encoding = "utf-8"))
    @Sql(value = "classpath:./sql/drop-users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            config = @SqlConfig(encoding = "utf-8"))
    void user_by_id_test_404() throws Exception {
        String uuid = UUID.randomUUID().toString();

        mvc.perform(get("/v1/users/" + uuid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @Sql(value = "classpath:./sql/insert-user.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(encoding = "utf-8"))
    @Sql(value = "classpath:./sql/drop-users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            config = @SqlConfig(encoding = "utf-8"))
    void subscribe_test_200() throws Exception {
        String userId = "68cdef45-e98e-4e86-91c8-b8fe437ae01f";
        String followerId = "71cdef45-e98e-4e86-91c8-b8fe437ae01f";

        mvc.perform(post("/v1/users/" + followerId + "/subscribe/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        mvc.perform(get("/v1/users/" + userId + "/followers")
                        .param("pageIndex", "0")
                        .param("pageSize", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response.content[0].id").value(followerId));
    }

    @Test
    @Sql(value = {"classpath:./sql/insert-user.sql", "classpath:./sql/insert-followers.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(encoding = "utf-8"))
    @Sql(value = "classpath:./sql/drop-users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            config = @SqlConfig(encoding = "utf-8"))
    void unsubscribe_test_200() throws Exception {
        String userId = "69cdef45-e98e-4e86-91c8-b8fe437ae01f";
        String followerId = "71cdef45-e98e-4e86-91c8-b8fe437ae01f";

        mvc.perform(post("/v1/users/" + followerId + "/unsubscribe/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        mvc.perform(get("/v1/users/" + userId + "/followers")
                        .param("pageIndex", "0")
                        .param("pageSize", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response.content").isEmpty());
    }

    @Test
    @Sql(value = "classpath:./sql/insert-user.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(encoding = "utf-8"))
    @Sql(value = "classpath:./sql/drop-users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            config = @SqlConfig(encoding = "utf-8"))
    void delete_test_200() throws Exception {
        String userId = "70cdef45-e98e-4e86-91c8-b8fe437ae01f";

        mvc.perform(delete("/v1/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @Sql(value = "classpath:./sql/insert-user.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(encoding = "utf-8"))
    @Sql(value = "classpath:./sql/drop-users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            config = @SqlConfig(encoding = "utf-8"))
    void createPhotoSuccess() throws Exception {
        doReturn(new PutObjectResult()).when(client).putObject(any());
        doNothing().when(client).deleteObject(any());

        try (InputStream inputStream = new ClassPathResource("image/test.jpg").getInputStream()) {
            byte[] bytes = StreamUtils.copyToByteArray(inputStream);
            MockMultipartFile file = new MockMultipartFile("files", "test.jpg",
                    MediaType.IMAGE_JPEG_VALUE, bytes);

            mvc.perform(multipart("/v1/users/68cdef45-e98e-4e86-91c8-b8fe437ae01f/avatar/save")
                            .file(file))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.response").value("/minio/post-photo-bucket/test.jpg"))
                    .andReturn();
        }
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
        return userInput;
    }

}
