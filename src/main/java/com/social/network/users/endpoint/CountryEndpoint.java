package com.social.network.users.endpoint;

import com.social.network.users.endpoint.mvc.Api;
import com.social.network.users.entity.Country;
import com.social.network.users.exceptions.ExceptionResponse;
import com.social.network.users.service.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Api
@Validated
@RequestMapping("/v1/country")
@Tag(name = "Country endpoint")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "${api.response-codes.ok.desc}"),
        @ApiResponse(responseCode = "400", content = {
                @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ExceptionResponse.class))},
                description = "${api.response-codes.badRequest.desc}"),
        @ApiResponse(responseCode = "404", content = {
                @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ExceptionResponse.class))},
                description = "${api.response-codes.notFound.desc}")
})
public class CountryEndpoint {

    private final CountryService countryService;

    public CountryEndpoint(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    @Operation(summary = "Поиск страны по имени")
    public ResponseEntity<List<Country>> searchCountry(@NotEmpty(message = "Field name is required")
                                                                            @Size(min = 2, max = 100)
                                                                            @RequestParam String name) {
        List<Country> countries = countryService.searchCountry(name);
        return ResponseEntity.ok(countries);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение страны по id")
    public ResponseEntity<Country> getCountryById(@PathVariable Long id) {
        Country country = countryService.getCountryById(id);
        return ResponseEntity.ok(country);
    }

}
