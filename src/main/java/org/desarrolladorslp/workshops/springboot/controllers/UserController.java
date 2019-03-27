package org.desarrolladorslp.workshops.springboot.controllers;

import org.desarrolladorslp.workshops.springboot.models.User;
import org.desarrolladorslp.workshops.springboot.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController //para que se pueda hacer como un rest controller sin necesidad de que haga una vista para la respuesta
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    //    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @PostMapping(value = "/user")
    public User user(@RequestParam(name = "email") String email,
                     @RequestParam(name = "name") String name) {

        return userService.createUser(email, name);
//        return "hola";
    }

    @GetMapping(value = "/user/{id}")
    public User getUser(@PathVariable(value = "id") Long id) {

        return userService.findById(id);
//        return "hola";
    }

    @DeleteMapping(value = "/user/{id}")
    public HttpStatus deleteUser(@PathVariable(value = "id") Long id) {
        userService.deleteUser(id);
        return HttpStatus.OK;
    }


}
