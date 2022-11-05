package com.social.network.users.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//todo: add field isDeleted

@Entity
@Table(name="users")
@Getter
@Setter
public class User extends BaseEntity {

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "surname", length = 50, nullable = false)
    private String surname;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "sex", length = 50, nullable = false)
    private UserSex sex;

    @Temporal(TemporalType.TIMESTAMP)
    private Date birthdate;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @Column(name = "avatar", length = 500)
    private String avatar;

    @Column(name = "user_description", length = 1000)
    private String userDescription;

    @Column(name = "nickname", length = 50, unique = true)
    private String nickname;

    @Column(name = "email", length = 100, unique = true)
    private String email;

    @Column(name = "phone_number", length = 30)
    private String phoneNumber;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_followers",
            joinColumns = {@JoinColumn(name = "user_id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "follower_id", nullable = false)})
    private List<User> followers = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_hard_skills",
            joinColumns = {@JoinColumn(name = "user_id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "hard_skill_id", nullable = false)})
    private List<HardSkill> hardSkills = new ArrayList<>();

    public User() {
    }

    public User(Integer version, String name, String surname, String lastName, UserSex sex, Date birthdate, Country country,
                String avatar, String userDescription, String nickname, String email, String phoneNumber) {
        this.setVersion(version);
        this.name = name;
        this.surname = surname;
        this.lastName = lastName;
        this.sex = sex;
        this.birthdate = birthdate;
        this.country = country;
        this.avatar = avatar;
        this.userDescription = userDescription;
        this.nickname = nickname;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
