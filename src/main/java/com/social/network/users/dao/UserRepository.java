package com.social.network.users.dao;

import com.social.network.users.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, UUID> {

    List<User> findAll();

    Page<User> findAllByCountry_Name(Pageable pageable, String countryName);

    @Query("SELECT u.followers FROM User u WHERE u.id = :userId")
    Page<User> getFollowers(UUID userId, Pageable pageable);

}
