package com.social.network.users.entity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.social.network.users.entity.Country;
import com.social.network.users.entity.HardSkill;
import com.social.network.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    @Builder.Default
    private List<HardSkillEntry> hardSkills = new ArrayList<>();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<UserEntry> followers;

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

}
