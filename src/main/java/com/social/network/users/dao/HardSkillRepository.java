package com.social.network.users.dao;

import com.social.network.users.entity.HardSkill;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HardSkillRepository extends CrudRepository<HardSkill, Long> {

    HardSkill findByName(String name);

}
