package com.social.network.users.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "HARD_SKILLS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HardSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @Access(AccessType.PROPERTY)
    private Long id;

    @Version
    @Column(name = "version", nullable = false)
    private Integer version;

    @Column(unique = true)
    private String name;

    public HardSkill(String name) {
        this.name = name;
    }
}
