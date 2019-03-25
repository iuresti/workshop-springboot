package org.desarrolladorslp.workshops.springboot.services;

import org.desarrolladorslp.workshops.springboot.models.User;

import java.util.List;


public interface UserService {
    User saveUser(User user);
    List<User> allUsers();
    User findByEmail(String email);
    User findById(Long id);
    void deleteUser(Long id);
    User updateUser(User user);
}
