package org.desarrolladorslp.workshops.springboot.service.impl;

import org.desarrolladorslp.workshops.springboot.models.User;
import org.desarrolladorslp.workshops.springboot.repository.UserRepository;
import org.desarrolladorslp.workshops.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

    //@Autowired inyectar //no es recomendado a nivel atributo
    private UserRepository userRepository;

    //INYECCION DE DEPENDENCIAS
    //inyectar por medio del constructor
    //cada que agregas una dependencia nueva se debe de modificar el constructor
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //inyectar por medio del setter SE DEBE DE PONER EL AUTWIRED
//    @Autowired
//    @Qualifier("nombre")
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(String email, String name) {
        User user = new User();

        user.setEmail(email);
        user.setName(name);


        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return null;
    }

    @Override
    public User findById(Long userId) {

        return userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User not found!"));
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {

        User user = findById(userId);

        userRepository.delete(user);

    }
}
