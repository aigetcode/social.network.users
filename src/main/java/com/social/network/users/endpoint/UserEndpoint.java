package com.social.network.users.endpoint;

import com.social.network.users.endpoint.mvc.Api;
import com.social.network.users.entity.User;
import com.social.network.users.dto.SortDto;
import com.social.network.users.dto.entry.UserEntry;
import com.social.network.users.dto.input.UserInput;
import com.social.network.users.exceptions.ExceptionResponse;
import com.social.network.users.service.UserService;
import com.social.network.users.util.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.UUID;

@Api
@Slf4j
@Validated
@RequestMapping("/v1/users")
@Tag(name = "User endpoint")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "${api.response-codes.ok.desc}"),
        @ApiResponse(responseCode = "400", content = {
                @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ExceptionResponse.class))},
                description = "${api.response-codes.badRequest.desc}"),
        @ApiResponse(responseCode = "404", content = {
                @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ExceptionResponse.class))},
                description = "${api.response-codes.notFound.desc}")
})
public class UserEndpoint {

    private final UserService userService;

    public UserEndpoint(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Получение пользователей постранично")
    public ResponseEntity<Page<UserEntry>> getPageUsers(@RequestParam(value = "pageIndex") int pageIndex,
                                          @RequestParam(value = "pageSize") int pageSize,
                                          @RequestParam(value = "country", required = false) String country,
                                          @RequestParam(value = "sorting", defaultValue = "id,desc",
                                                  required = false) String[] sortEntries) {
        Page<UserEntry> users = userService.getPageUsers(pageIndex, pageSize, country, SortDto.toSort(sortEntries));
        return ResponseEntity.ok(users);
    }

    @GetMapping("{id}/followers")
    @Operation(summary = "Получение подписчиков пользователя постранично")
    public ResponseEntity<Page<UserEntry>> getPageFollowersByUserId(@PathVariable(value = "id") String userId,
                                                       @RequestParam(value = "pageIndex") int pageIndex,
                                                       @RequestParam(value = "pageSize") int pageSize,
                                                       @RequestParam(value = "sorting", defaultValue = "id,desc",
                                                               required = false) String[] sortEntries) {
        Page<UserEntry> users = userService.getFollowersByUserId(pageIndex, pageSize,
                UUID.fromString(userId), SortDto.toSort(sortEntries));
        return ResponseEntity.ok(users);
    }

    @GetMapping("/all")
    @Operation(summary = "Получение всех пользователей")
    public ResponseEntity<List<UserEntry>> getAllUsers() {
        log.info(">> GET users/all :: Получение всех пользователей");
        List<UserEntry> users = userService.getAllUsers(false);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение пользователя по id")
    public ResponseEntity<UserEntry> getUserById(@PathVariable String id) {
        UUID uuid = UUID.fromString(id);
        UserEntry user = userService.getUserById(uuid);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Добавление пользователя")
    public ResponseEntity<String> createUser(@Valid @RequestBody UserInput userInput) {
        User user = Utils.createUser(userInput);
        UUID uuid = userService.createUser(user, userInput.getHardSkills());
        return ResponseEntity.ok(uuid.toString());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновление пользователя")
    public ResponseEntity<String> updateUser(@Valid @RequestBody UserInput userInput,
                                        @PathVariable String id) {
        User user = Utils.createUser(userInput);
        UUID uuid = userService.updateUser(UUID.fromString(id), user, userInput.getHardSkills());
        return ResponseEntity.ok(uuid.toString());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление пользователя")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(UUID.fromString(id));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{followerId}/subscribe/{userId}")
    @Operation(summary = "Подписаться на пользователя")
    public ResponseEntity<Void> subscribe(@PathVariable String userId,
                                       @PathVariable String followerId) {
        userService.subscribe(UUID.fromString(userId), UUID.fromString(followerId));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{followerId}/unsubscribe/{userId}")
    @Operation(summary = "Отписаться от пользователя")
    public ResponseEntity<Void> unsubscribe(@PathVariable String userId,
                                         @PathVariable String followerId) {
        userService.unsubscribe(UUID.fromString(userId), UUID.fromString(followerId));
        return ResponseEntity.ok().build();
    }

}
