package org.desarrolladorslp.workshops.springboot.forms;

import javax.validation.constraints.NotEmpty;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiObject(name = "LoginForm", description = "Formulario de login de usuario.")
public class LoginForm {
    @NotEmpty
    @ApiObjectField(order = 1, description = "Identificador de Usuario", required = true)
    private String username;
    @NotEmpty
    @ApiObjectField(order = 2, description = "Password", required = true)
    private String password;
}
