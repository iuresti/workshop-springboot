package org.desarrolladorslp.workshops.springboot.service;

import org.desarrolladorslp.workshops.springboot.models.User;

public interface UserService {

    User createUser(String email, String name);

    User findByEmail(String email);

    User findById(Long userId);

    void deleteUser(Long userId);

}
