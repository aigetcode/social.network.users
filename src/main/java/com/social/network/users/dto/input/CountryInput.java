package com.social.network.users.dto.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CountryInput {

    private Long id;
    @NotEmpty
    private String name;
    private String region;
    private String subregion;

}
