package com.social.network.users.entity.dto;

import com.social.network.users.entity.Country;
import com.social.network.users.entity.HardSkill;
import com.social.network.users.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.Date;
import java.util.List;

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

    private List<HardSkillEntry> hardSkills;
    private List<FollowerEntry> followers;

    public static List<UserEntry> fromListUsers(List<User> users) {
        if (users.isEmpty())
            return Collections.emptyList();

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

        if (user.getSex() != null)
            userEntry.setSex(user.getSex().toString());

        userEntry.setBirthdate(user.getBirthdate());
        userEntry.setCountry(user.getCountry());
        userEntry.setAvatar(user.getAvatar());
        userEntry.setUserDescription(user.getUserDescription());
        userEntry.setNickname(user.getNickname());
        userEntry.setEmail(user.getEmail());
        userEntry.setPhoneNumber(user.getPhoneNumber());

        setHardSkills(user.getHardSkills(), userEntry);
        setFollowers(user.getFollowers(), userEntry);

        return userEntry;
    }

    private static void setHardSkills(List<HardSkill> hardSkills, UserEntry userEntry) {
        if (!hardSkills.isEmpty()) {
            List<HardSkillEntry> hardSkillEntries = hardSkills.stream()
                    .map(skill -> new HardSkillEntry(skill.getName()))
                    .toList();
            userEntry.setHardSkills(hardSkillEntries);
        } else {
            userEntry.setHardSkills(Collections.emptyList());
        }
    }

    private static void setFollowers(List<User> followers, UserEntry userEntry) {
        if (!followers.isEmpty()) {
            List<FollowerEntry> followerEntries = followers.stream().map(follower -> {
                FollowerEntry followerEntry = new FollowerEntry();
                followerEntry.setId(follower.getId().toString());
                followerEntry.setName(follower.getName());
                followerEntry.setSurname(follower.getSurname());
                return followerEntry;
            }).toList();
            userEntry.setFollowers(followerEntries);
        } else {
            userEntry.setFollowers(Collections.emptyList());
        }
    }

}
