package org.desarrolladorslp.workshops.springboot.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationForm {
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    @Size(min = 4, max = 85)
    private String username;
    @NotEmpty
    @Size(min = 5, max = 80)
    private String name;
    @NotEmpty
    @Size(min = 8, max = 30)
    private String password;
}
