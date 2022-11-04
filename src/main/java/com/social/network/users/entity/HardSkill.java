package com.social.network.users.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "hard_skills")
@Getter
@Setter
public class HardSkill extends BaseEntity {

    private String name;

}
