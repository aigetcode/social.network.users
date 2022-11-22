package com.social.network.users.endpoint;

import com.social.network.users.endpoint.mvc.SuccessResponse;
import com.social.network.users.entity.Country;
import com.social.network.users.dto.input.CountryInput;
import com.social.network.users.exceptions.ExceptionResponse;
import com.social.network.users.service.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Operation(summary = "Добавление страны")
    @PostMapping
    public ResponseEntity<SuccessResponse<Long>> saveCountry(@Valid @RequestBody CountryInput countryInput) {
        Country country = new Country(countryInput.getName(), countryInput.getRegion(), countryInput.getSubregion());
        Long countryId = countryService.saveCountry(country);
        return ResponseEntity.ok(SuccessResponse.of(countryId));
    }

    @Operation(summary = "Поиск страны по имени")
    @GetMapping
    public ResponseEntity<SuccessResponse<List<Country>>> searchCountry(@NotEmpty(message = "Field name is required")
                                                                            @Size(min = 2, max = 100)
                                                                            @RequestParam String name) {
        List<Country> countries = countryService.searchCountry(name);
        return ResponseEntity.ok(SuccessResponse.of(countries));
    }

    @Operation(summary = "Получение страны по id")
    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<Country>> getCountryById(@PathVariable Long id) {
        Country country = countryService.getCountryById(id);
        return ResponseEntity.ok(SuccessResponse.of(country));
    }

}
