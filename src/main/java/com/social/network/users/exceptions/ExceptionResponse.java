package com.social.network.users.exceptions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ExceptionResponse<T> {

    public static final String ERROR_FIELD = "error";

    private final Boolean success;
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
