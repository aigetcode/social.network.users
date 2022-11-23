package com.social.network.users.dto.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

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
