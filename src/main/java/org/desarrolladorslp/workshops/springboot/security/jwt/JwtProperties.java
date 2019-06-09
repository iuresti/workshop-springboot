package org.desarrolladorslp.workshops.springboot.security.jwt;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "security.jwt.token")
@Data
@Validated
public class JwtProperties {

    @NotBlank
    private String secretKey;
    @Min(900)
    private long expireLength;
    @NotBlank
    private String issuer;

}
