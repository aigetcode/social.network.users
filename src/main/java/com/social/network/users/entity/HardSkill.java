package com.social.network.users.entity;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "HARD_SKILLS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HardSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
