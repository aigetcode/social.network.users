package com.social.network.users.endpoint;

import com.social.network.users.endpoint.mvc.SuccessResponse;
import com.social.network.users.entity.Country;
import com.social.network.users.entity.User;
import com.social.network.users.entity.UserSex;
import com.social.network.users.entity.dto.UserEntry;
import com.social.network.users.entity.dto.UserInput;
import com.social.network.users.service.UserService;
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
                                          @RequestParam(value = "country") String country) {
        Page<UserEntry> users = userService.getPageUsers(pageIndex, pageSize, country);
        return ResponseEntity.ok(SuccessResponse.of(users));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        List<UserEntry> users = userService.getAllUsers();
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
        Country country = new Country(UUID.fromString(userInput.getCountry()));
        User user = new User(userInput.getVersion(), userInput.getName(), userInput.getSurname(),
                userInput.getLastName(), UserSex.valueOf(userInput.getSex()), userInput.getBirthdate(),
                country, userInput.getAvatar(), userInput.getUserDescription(), userInput.getNickname(),
                userInput.getEmail(), userInput.getPhoneNumber());
        UUID uuid = userService.createUser(user, userInput.getHardSkills());
        return ResponseEntity.ok(SuccessResponse.of(uuid.toString()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserInput userInput,
                                        @PathVariable String id) {
        Country country = new Country(UUID.fromString(userInput.getCountry()));
        User user = new User(userInput.getVersion(), userInput.getName(), userInput.getSurname(),
                userInput.getLastName(), UserSex.valueOf(userInput.getSex()), userInput.getBirthdate(),
                country, userInput.getAvatar(), userInput.getUserDescription(), userInput.getNickname(),
                userInput.getEmail(), userInput.getPhoneNumber());
        UUID uuid = userService.updateUser(UUID.fromString(id), user, userInput.getHardSkills());
        return ResponseEntity.ok(SuccessResponse.of(uuid.toString()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        userService.deleteUser(UUID.fromString(id));
        return ResponseEntity.ok(SuccessResponse.of(null));
    }

    @PostMapping("/{followerId}/subscribe/{userId}")
    public ResponseEntity<?> subscribe(@PathVariable String userId,
                                       @PathVariable String followerId) {
        userService.subscribe(UUID.fromString(userId), UUID.fromString(followerId));
        return ResponseEntity.ok(SuccessResponse.of(null));
    }

    @PostMapping("/{followerId}/unsubscribe/{userId}")
    public ResponseEntity<?> unsubscribe(@PathVariable String userId,
                                         @PathVariable String followerId) {
        userService.unsubscribe(UUID.fromString(userId), UUID.fromString(followerId));
        return ResponseEntity.ok(SuccessResponse.of(null));
    }

}