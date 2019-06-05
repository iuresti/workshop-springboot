package org.desarrolladorslp.workshops.springboot.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiObject(name = "JwtTokenVM", description = "VM para representar token JWT.")
public class JwtTokenVM {
    @ApiObjectField(description = "Token JWT")
    private String token;
    @ApiObjectField(description = "Expiracion token en segundos")
    private long expiresIn;
}
