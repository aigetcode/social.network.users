package com.social.network.users.endpoint.mvc;

import com.social.network.users.exceptions.ExceptionResponse;
import lombok.SneakyThrows;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

@ControllerAdvice
public class RestAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    @SneakyThrows
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {
        if (body instanceof ExceptionResponse) {
            return ExceptionResponse.builder()
                    .success(false)
                    .error(body)
                    .build();
        }

        if (returnType.getContainingClass().isAnnotationPresent(Api.class)
                && !Objects.requireNonNull(returnType.getMethod()).isAnnotationPresent(IgnoreResponseBinding.class)) {
            return SuccessResponse.builder()
                    .success(true)
                    .response(body)
                    .build();
        }
        return body;
    }
}
