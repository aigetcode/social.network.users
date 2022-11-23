package com.social.network.users.endpoint;

import com.social.network.users.endpoint.mvc.SuccessResponse;
import com.social.network.users.entity.User;
import com.social.network.users.dto.SortDto;
import com.social.network.users.dto.entry.UserEntry;
import com.social.network.users.dto.input.UserInput;
import com.social.network.users.service.UserService;
import com.social.network.users.util.Utils;
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
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/v1/users")
public class UserEndpoint {

    private final UserService userService;

    public UserEndpoint(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getPageUsers(@RequestParam(value = "pageIndex") int pageIndex,
                                          @RequestParam(value = "pageSize") int pageSize,
                                          @RequestParam(value = "country", required = false) String country,
                                          @RequestParam(value = "sorting", defaultValue = "id,desc",
                                                  required = false) String[] sortEntries) {
        Page<UserEntry> users = userService.getPageUsers(pageIndex, pageSize, country, SortDto.toSort(sortEntries));
        return ResponseEntity.ok(SuccessResponse.of(users));
    }

    @GetMapping("{id}/followers")
    public ResponseEntity<?> getPageFollowersByUserId(@PathVariable(value = "id") String userId,
                                                       @RequestParam(value = "pageIndex") int pageIndex,
                                                       @RequestParam(value = "pageSize") int pageSize,
                                                       @RequestParam(value = "sorting", defaultValue = "id,desc",
                                                               required = false) String[] sortEntries) {
        Page<UserEntry> users = userService.getFollowersByUserId(pageIndex, pageSize,
                UUID.fromString(userId), SortDto.toSort(sortEntries));
        return ResponseEntity.ok(SuccessResponse.of(users));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        List<UserEntry> users = userService.getAllUsers(false);
        return ResponseEntity.ok(SuccessResponse.of(users));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        UUID uuid = UUID.fromString(id);
        UserEntry user = userService.getUserById(uuid);
        return ResponseEntity.ok(SuccessResponse.of(user));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createUser(@Valid @RequestBody UserInput userInput) {
        User user = Utils.createUser(userInput);
        UUID uuid = userService.createUser(user, userInput.getHardSkills());
        return ResponseEntity.ok(SuccessResponse.of(uuid.toString()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserInput userInput,
                                        @PathVariable String id) {
        User user = Utils.createUser(userInput);
        UUID uuid = userService.updateUser(UUID.fromString(id), user, userInput.getHardSkills());
        return ResponseEntity.ok(SuccessResponse.of(uuid.toString()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        userService.deleteUser(UUID.fromString(id));
        return ResponseEntity.ok(SuccessResponse.ok());
    }

    @PostMapping("/{followerId}/subscribe/{userId}")
    public ResponseEntity<?> subscribe(@PathVariable String userId,
                                       @PathVariable String followerId) {
        userService.subscribe(UUID.fromString(userId), UUID.fromString(followerId));
        return ResponseEntity.ok(SuccessResponse.ok());
    }

    @PostMapping("/{followerId}/unsubscribe/{userId}")
    public ResponseEntity<?> unsubscribe(@PathVariable String userId,
                                         @PathVariable String followerId) {
        userService.unsubscribe(UUID.fromString(userId), UUID.fromString(followerId));
        return ResponseEntity.ok(SuccessResponse.ok());
    }

}
