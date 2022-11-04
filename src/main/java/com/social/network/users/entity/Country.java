package com.social.network.users.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
@Getter
@Setter
public class Country extends BaseEntity {

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "region")
    private String region;

    @Column(name = "subregion")
    private String subregion;

}
