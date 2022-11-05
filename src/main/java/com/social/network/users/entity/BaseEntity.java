package com.social.network.users.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
public abstract class BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    @Access(AccessType.PROPERTY)
    private UUID id;

    @Version
    @Column(name = "version", nullable = false)
    private Integer version;

    public BaseEntity(UUID id) {
        this.id = id;
    }
}
