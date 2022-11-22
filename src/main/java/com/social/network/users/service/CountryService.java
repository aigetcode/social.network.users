package com.social.network.users.service;

import com.social.network.users.dao.CountryRepository;
import com.social.network.users.entity.Country;
import com.social.network.users.exceptions.NotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CountryService {

    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public Long saveCountry(Country country) {
        return countryRepository.save(country).getId();
    }

    public List<Country> searchCountry(String countryName) {
        Pageable pageable = Pageable.ofSize(10);
        return countryRepository.searchByNameLike(countryName.toLowerCase(), pageable);
    }

    public Country getCountryById(Long countryId) {
        return countryRepository.findById(countryId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Country not found"));
    }

}
