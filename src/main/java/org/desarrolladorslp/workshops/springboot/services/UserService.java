package org.desarrolladorslp.workshops.springboot.services;

import org.desarrolladorslp.workshops.springboot.models.User;

import java.util.List;


public interface UserService {
    User saveUser(User user);
    List<User> allUsers();
    User findByEmail(String email);
    User findById(Long id) throws Exception;
    void deleteUser(Long id) throws Exception;
    User updateUser(User user);
}
