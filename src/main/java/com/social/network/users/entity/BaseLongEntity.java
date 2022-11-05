package com.social.network.users.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import java.util.UUID;

public class BaseLongEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    @Access(AccessType.PROPERTY)
    private UUID id;

    @Version
    @Column(name = "version", nullable = false)
    private Integer version;

}
