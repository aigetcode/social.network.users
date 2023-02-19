package com.social.network.users.dto.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Schema(description = "Входные данные пользователя")
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
    private Long country;
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
