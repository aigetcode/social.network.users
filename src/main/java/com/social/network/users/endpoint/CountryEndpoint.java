package com.social.network.users.endpoint;

import com.social.network.users.endpoint.mvc.SuccessResponse;
import com.social.network.users.entity.Country;
import com.social.network.users.entity.dto.CountryInput;
import com.social.network.users.service.CountryService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Validated
@RestController
@RequestMapping("/v1/country")
public class CountryEndpoint {

    private final CountryService countryService;

    public CountryEndpoint(CountryService countryService) {
        this.countryService = countryService;
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<Long>> saveCountry(@Valid @RequestBody CountryInput countryInput) {
        Country country = new Country(countryInput.getName(), countryInput.getRegion(), countryInput.getSubregion());
        Long countryId = countryService.saveCountry(country);
        return ResponseEntity.ok(SuccessResponse.of(countryId));
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<List<Country>>> searchCountry(@NotEmpty(message = "Field name is required")
                                                                            @Size(min = 2, max = 100)
                                                                            @RequestParam String name) {
        List<Country> countries = countryService.searchCountry(name);
        return ResponseEntity.ok(SuccessResponse.of(countries));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<Country>> getCountryById(@PathVariable Long id) {
        Country country = countryService.getCountryById(id);
        return ResponseEntity.ok(SuccessResponse.of(country));
    }

}
