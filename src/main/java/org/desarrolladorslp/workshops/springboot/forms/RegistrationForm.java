package org.desarrolladorslp.workshops.springboot.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiObject(name = "RegistrationForm", description = "Formulario de registro de usuario.")
public class RegistrationForm {
    @NotEmpty
    @Email
    @ApiObjectField(description = "Email de usuario", required = true)
    private String email;
    @NotEmpty
    @Size(min = 4, max = 85)
    @ApiObjectField(description = "Identificador unico de usuario", required = true)
    private String username;
    @NotEmpty
    @Size(min = 5, max = 80)
    @ApiObjectField(description = "Nombre de usuario", required = true)
    private String name;
    @NotEmpty
    @Size(min = 8, max = 30)
    @ApiObjectField(description = "Password de usuario", required = true)
    private String password;
}
