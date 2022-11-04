package com.social.network.users.entity.dto;

import com.social.network.users.entity.Country;
import com.social.network.users.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class UserEntry {

    private String id;
    private Integer version;
    private String name;
    private String surname;
    private String lastName;
    private String sex; // UserSex
    private Date birthdate;
    private Country country;
    private String avatar;
    private String userDescription;
    private String nickname;
    private String email;
    private String phoneNumber;

    private List<FollowerEntry> followers;

    public static List<UserEntry> fromListUsers(List<User> users) {
        return users.stream()
                .map(UserEntry::fromUser)
                .toList();
    }

    public static UserEntry fromUser(User user) {
        if (user == null)
            return null;

        UserEntry userEntry = new UserEntry();

        if (user.getId() != null) {
            userEntry.setId(user.getId().toString());
        }

        userEntry.setVersion(user.getVersion());
        userEntry.setName(user.getName());
        userEntry.setSurname(user.getSurname());
        userEntry.setLastName(user.getLastName());
        userEntry.setSex(user.getSex().toString());
        userEntry.setBirthdate(user.getBirthdate());
        userEntry.setCountry(user.getCountry());
        userEntry.setAvatar(user.getAvatar());
        userEntry.setUserDescription(user.getUserDescription());
        userEntry.setNickname(user.getNickname());
        userEntry.setEmail(user.getEmail());
        userEntry.setPhoneNumber(user.getPhoneNumber());

        List<User> followers = user.getFollowers();
        if (!followers.isEmpty()) {
            List<FollowerEntry> followerEntries = followers.stream().map(follower -> {
                FollowerEntry followerEntry = new FollowerEntry();
                followerEntry.setId(follower.getId().toString());
                followerEntry.setName(follower.getName());
                followerEntry.setSurname(followerEntry.getSurname());
                return followerEntry;
            }).toList();
            userEntry.setFollowers(followerEntries);
        } else {
            userEntry.setFollowers(Collections.emptyList());
        }
        return userEntry;
    }

}
