package org.desarrolladorslp.workshops.springboot.controllers;

import org.desarrolladorslp.workshops.springboot.models.User;
import org.desarrolladorslp.workshops.springboot.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    @ResponseBody
    private HttpStatus createUser(@RequestParam(value = "email", required = true) String email, @RequestParam(value = "name", required = true) String name) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        userService.saveUser(user);
        return HttpStatus.OK;
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    private User getUserByEmail(@RequestParam(value="email", required = true)String email) {
        return userService.findByEmail(email);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    @ResponseBody
    private User getUsersById(@PathVariable("id")Long id) throws Exception {
        return userService.findById(id);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    private HttpStatus deleteUser(@PathVariable("id")Long id) throws Exception {
        userService.deleteUser(id);
        return HttpStatus.OK;
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.PATCH)
    @ResponseBody
    private HttpStatus updateUser(@PathVariable("id")Long id,@RequestParam(value = "email", required = true) String email, @RequestParam(value = "name", required = true) String name) throws Exception {
        User user = userService.findById(id);
        user.setName(name);
        user.setEmail(email);
        userService.updateUser(user);
        return HttpStatus.OK;
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @ResponseBody
    private List<User> getAllUsers() {
        return userService.allUsers();
    }
}
