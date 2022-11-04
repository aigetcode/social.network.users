package com.social.network.users.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "hard_skills")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HardSkill extends BaseEntity {

    @Column(unique = true)
    private String name;

}
