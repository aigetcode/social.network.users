package com.social.network.users.endpoint.mvc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.social.network.users.dto.entry.UserEntry;
import com.social.network.users.entity.Country;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class SuccessResponse<T> {

    private final Boolean success;

    @Schema(oneOf = { List.class, Long.class, UserEntry.class, Country.class, Page.class })
    private final T response;

    @JsonCreator
    public SuccessResponse(@JsonProperty("success") Boolean success,
                           @JsonProperty("response") T response) {
        this.success = success;
        this.response = response;
    }

    public static <T> SuccessResponse<T> of(T t) {
        return new SuccessResponse<>(true, t);
    }

    public static <T> SuccessResponse<T> ok() {
        return new SuccessResponse<>(true, null);
    }

}
