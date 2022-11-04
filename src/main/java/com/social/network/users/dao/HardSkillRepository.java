package com.social.network.users.dao;

import com.social.network.users.entity.HardSkill;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface HardSkillRepository extends CrudRepository<HardSkill, UUID> {

    HardSkill findByName(String name);

}
