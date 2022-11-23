package com.social.network.users.endpoint.mvc;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@RestController
@IgnoreResponseBinding
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Api {
    @AliasFor(
            annotation = Controller.class
    )
    String value() default "";
}
