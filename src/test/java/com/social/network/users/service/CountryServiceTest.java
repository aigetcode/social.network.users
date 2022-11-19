package com.social.network.users.service;

import com.social.network.users.dao.CountryRepository;
import com.social.network.users.entity.Country;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CountryServiceTest {

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CountryService countryService;

    @Test
    void saveCountryTest() {
        Country country = Country.builder()
                .id(123L)
                .name("country")
                .region("region")
                .subregion("subregion")
                .build();

        doReturn(country).when(countryRepository).save(any());

        countryService.saveCountry(country);
        verify(countryRepository).save(country);
    }

    @Test
    void searchCountryTest() {
        Country country = Country.builder()
                .id(123L)
                .name("country")
                .region("region")
                .subregion("subregion")
                .build();

        doReturn(List.of(country)).when(countryRepository).searchByNameLike(any(), any());

        countryService.searchCountry("country");
        verify(countryRepository).searchByNameLike(any(), any());
    }

    @Test
    void getCountryByIdTest() {
        Country country = Country.builder()
                .id(123L)
                .name("country")
                .region("region")
                .subregion("subregion")
                .build();

        doReturn(Optional.of(country)).when(countryRepository).findById(123L);

        countryService.getCountryById(123L);
        verify(countryRepository).findById(123L);
    }

}
