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
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @Access(AccessType.PROPERTY)
    private Long id;

    @Version
    @Column(name = "version", nullable = false)
    private Integer version;

    @Column(name = "name", length = 100, unique = true)
    private String name;

    @Column(name = "region")
    private String region;

    @Column(name = "subregion")
    private String subregion;

    public Country(Long id) {
        this.id = id;
    }

    public Country(String name, String region, String subregion) {
        this.name = name;
        this.region = region;
        this.subregion = subregion;
    }
}
