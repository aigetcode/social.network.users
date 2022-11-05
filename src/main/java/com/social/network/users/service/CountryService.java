package com.social.network.users.service;

import com.social.network.users.dao.CountryRepository;
import com.social.network.users.entity.Country;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public record CountryService(CountryRepository countryRepository) {

    public UUID saveCountry(Country country) {
        return countryRepository.save(country).getId();
    }

    public List<Country> searchCountry(String countryName) {
        Pageable pageable = Pageable.ofSize(10);
        return countryRepository.searchByNameLike(countryName.toLowerCase(), pageable);
    }

    public Country getCountryById(UUID countryId) {
        return countryRepository.findById(countryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Country not found"));
    }

}
