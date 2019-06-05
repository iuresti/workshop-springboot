package org.desarrolladorslp.workshops.springboot.controllers;

import org.desarrolladorslp.workshops.springboot.models.User;
import org.desarrolladorslp.workshops.springboot.services.UserService;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiAuthToken;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiPathParam;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@Api(name="User Resource", description = "Administracion de Users.")
@ApiAuthToken(scheme = "Bearer", roles = "ADMIN")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Admin Role
    @RequestMapping(method = RequestMethod.POST)
    @ApiMethod(description = "Crear un nuevo usuario.")
    public ResponseEntity<User> create(@RequestBody User user) {
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
    }

    // Admin Role
    @GetMapping(value = "/email/{email}/")
    @ApiMethod(description = "Recuperar Usuario mediante email.")
    public ResponseEntity<User> getByEmail(
            @ApiPathParam(name = "email", description = "Email de Usuario") @PathVariable String email) {
        return new ResponseEntity<>(userService.findByEmail(email), HttpStatus.OK);
    }

    // Admin Role
    @GetMapping(value = "/{id}")
    @ApiMethod(description = "Recuperar Usuario mediante id.")
    public ResponseEntity<User> getById(
            @ApiPathParam(name = "id", description = "Id de Usuario") @PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    // Admin Role
    @DeleteMapping(value = "/{id}")
    @ApiMethod(description = "Eliminar Usuario")
    public ResponseEntity delete(
            @ApiPathParam(name = "id", description = "Id de Usuario") @PathVariable("id") Long id) {

        userService.deleteById(id);

        return new ResponseEntity(HttpStatus.OK);
    }

    // Admin Role
    @PutMapping
    @ApiMethod(description = "Actualizar Usuario")
    public ResponseEntity<User> update(@RequestBody User user) {
        return new ResponseEntity<>(userService.updateUser(user), HttpStatus.OK);
    }

    // Admin Role
    @GetMapping
    @ApiMethod(description = "Recuperar Usuarios")
    public ResponseEntity<List<User>> list() {
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }
}
