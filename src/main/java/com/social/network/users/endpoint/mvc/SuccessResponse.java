package com.social.network.users.endpoint.mvc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class SuccessResponse<T> {

    private final Boolean success;
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

}
