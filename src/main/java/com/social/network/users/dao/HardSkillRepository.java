package com.social.network.users.dao;

import com.social.network.users.entity.HardSkill;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HardSkillRepository extends CrudRepository<HardSkill, Long> {

    HardSkill findByName(String name);

}
