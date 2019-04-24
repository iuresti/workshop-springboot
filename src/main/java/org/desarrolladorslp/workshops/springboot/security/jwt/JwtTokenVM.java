package org.desarrolladorslp.workshops.springboot.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtTokenVM {
    private String token;
    private long expiresIn;
}
