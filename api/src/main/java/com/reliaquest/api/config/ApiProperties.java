package com.reliaquest.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "external.api")
@Getter
@Setter
public class ApiProperties {
    private String baseUrl;

}
