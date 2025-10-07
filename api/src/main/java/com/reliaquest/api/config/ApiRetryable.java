package com.reliaquest.api.config;

import java.lang.annotation.*;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.HttpClientErrorException.TooManyRequests;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Retryable(
        include = {TooManyRequests.class},
        exclude = {Exception.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 3000, multiplier = 2))
public @interface ApiRetryable {}
