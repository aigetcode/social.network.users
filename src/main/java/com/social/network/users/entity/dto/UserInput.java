package com.social.network.users.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInput {

    private String id;
    private Integer version;
    @NotEmpty
    @Size(max = 50)
    private String name;
    @NotEmpty
    @Size(max = 50)
    private String surname;
    private String lastName;
    @NotEmpty
    private String sex; // UserSex
    private Date birthdate;
    private String country;
    private String avatar;
    @Size(max = 1000)
    private String userDescription;
    @Size(max = 50)
    private String nickname;
    @Email
    private String email;
    private String phoneNumber;

    private List<String> hardSkills;

}
