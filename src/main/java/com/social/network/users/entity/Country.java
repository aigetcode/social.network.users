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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "COUNTRY")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Access(AccessType.PROPERTY)
    private Long id;

    @Version
    @Column(name = "version", nullable = false)
    private Integer version;

    @Column(name = "name", length = 100, unique = true, nullable = false)
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
