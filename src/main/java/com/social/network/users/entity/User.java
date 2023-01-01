package com.social.network.users.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "USERS")
@SQLDelete(sql = "UPDATE USERS SET deleted = true WHERE id=? and version=?")
@FilterDef(name = "deletedProductFilter", parameters = @ParamDef(name = "isDeleted", type = "boolean"))
@Filter(name = "deletedProductFilter", condition = "deleted = :isDeleted")
public class User {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @ColumnDefault("random_uuid()")
    @Column(name = "id", nullable = false)
    @Access(AccessType.PROPERTY)
    private UUID id;

    @Version
    @Column(name = "version", nullable = false)
    private Integer version;

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

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_followers",
            joinColumns = {@JoinColumn(name = "user_id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "follower_id", nullable = false)})
    private List<User> followers = new ArrayList<>();

    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_hard_skills",
            joinColumns = {@JoinColumn(name = "user_id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "hard_skill_id", nullable = false)})
    private List<HardSkill> hardSkills = new ArrayList<>();

    @Builder.Default
    private boolean deleted = Boolean.FALSE;

    public User() {
    }

    public User(Integer version, String name, String surname, String lastName, UserSex sex, Date birthdate, Country country,
                String avatar, String userDescription, String nickname, String email, String phoneNumber) {
        this.version = version;
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

    public List<User> getFollowers() {
        if (this.followers == null) {
            return new ArrayList<>();
        }

        return followers;
    }

    public List<HardSkill> getHardSkills() {
        if (this.hardSkills == null) {
            return new ArrayList<>();
        }

        return hardSkills;
    }
}
