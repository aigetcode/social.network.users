package com.social.network.users.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Country extends BaseEntity {

    @Column(name = "name", length = 100, unique = true)
    private String name;

    @Column(name = "region")
    private String region;

    @Column(name = "subregion")
    private String subregion;

    public Country(UUID id) {
        super(id);
    }
}
