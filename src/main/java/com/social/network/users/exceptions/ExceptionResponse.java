package com.social.network.users.exceptions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ExceptionResponse<T> {

    @Schema(defaultValue = "false")
    private final Boolean success;

    @Schema(anyOf = { BaseErrorResponse.class })
    private final T error;

    @JsonCreator
    public ExceptionResponse(@JsonProperty("success") Boolean success,
                             @JsonProperty("error") T error) {
        this.success = success;
        this.error = error;
    }

    public static <T> ExceptionResponse<T> from(Throwable ex, T body) {
        if (ex == null) {
            return null;
        }

        return new ExceptionResponse<>(false, body);
    }

}
