package org.desarrolladorslp.workshops.springboot.controllers;

import org.desarrolladorslp.workshops.springboot.forms.RegistrationForm;
import org.desarrolladorslp.workshops.springboot.services.UserService;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiError;
import org.jsondoc.core.annotation.ApiErrors;
import org.jsondoc.core.annotation.ApiMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api")
@Api(name="Account Resource", description = "Administracion de cuenta de usuario.")
public class AccountController {

    private final UserService userService;

    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiMethod(description = "Registrar usuario dentro de la aplicacion.")
    @ApiErrors(apierrors = {
            @ApiError(code = "409", description = "Username ya registrado"),
            @ApiError(code = "409", description = "Email ya registrado")
    })
    public void registerUser(@Valid @RequestBody RegistrationForm registrationForm) {
        userService.createUser(registrationForm);
    }
}
