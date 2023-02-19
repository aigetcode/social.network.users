package com.social.network.users.dao;

import com.social.network.users.entity.Country;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;



@Repository
public interface CountryRepository extends CrudRepository<Country, Long> {

    @Query("SELECT c FROM Country c WHERE LOWER(c.name) LIKE %:name% ORDER BY c.name ASC")
    List<Country> searchByNameLike(String name, Pageable pageable);

}
