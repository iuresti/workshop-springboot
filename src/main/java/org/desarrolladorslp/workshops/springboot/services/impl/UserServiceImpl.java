package org.desarrolladorslp.workshops.springboot.services.impl;

import org.desarrolladorslp.workshops.springboot.exceptions.EmailAlreadyRegisteredException;
import org.desarrolladorslp.workshops.springboot.exceptions.InternalServerException;
import org.desarrolladorslp.workshops.springboot.exceptions.UsernameAlreadyRegisteredException;
import org.desarrolladorslp.workshops.springboot.forms.RegistrationForm;
import org.desarrolladorslp.workshops.springboot.models.Role;
import org.desarrolladorslp.workshops.springboot.models.User;
import org.desarrolladorslp.workshops.springboot.models.RoleName;
import org.desarrolladorslp.workshops.springboot.repository.RoleRepository;
import org.desarrolladorslp.workshops.springboot.repository.UserRepository;
import org.desarrolladorslp.workshops.springboot.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("Username not found"));
    }

    @Override
    public User findById(Long id) throws EntityNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public void deleteById(Long id) throws EntityNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        userRepository.delete(user);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User createUser(RegistrationForm registrationForm) {

        if(Objects.isNull(registrationForm) || Objects.isNull(registrationForm.getEmail()) ||
                Objects.isNull(registrationForm.getUsername()) || Objects.isNull(registrationForm.getName())
                || Objects.isNull(registrationForm.getPassword())) {
            throw new IllegalArgumentException("registrationForm must not be null or contain empty values");
        }

        // Check if the given email has already been registered.
        userRepository.findByEmail(registrationForm.getEmail().toLowerCase())
                .ifPresent(user -> {
            throw new EmailAlreadyRegisteredException(
                    "Email " + registrationForm.getEmail() + " has already been registered");
        });

        // Check if the given username has already been registered.
        userRepository.findByUsername(registrationForm.getUsername().toLowerCase())
                .ifPresent(user -> {
                    throw new UsernameAlreadyRegisteredException(
                            "Username " + registrationForm.getEmail() + " has already been registered");
                });

        final Set<Role> rolesList = new HashSet<>();
        roleRepository.findByName(RoleName.ROLE_USER).ifPresent(rolesList::add);

        if(rolesList.isEmpty()) {
            throw new InternalServerException("An error has occurred while setting new user roles");
        }

        final User newUser = User.builder()
                .email(registrationForm.getEmail().toLowerCase())
                .username(registrationForm.getUsername().toLowerCase())
                .name(registrationForm.getName().toLowerCase())
                .password(passwordEncoder.encode(registrationForm.getPassword()))
                .enabled(true)
                .authorities(rolesList)
                .build();

        return userRepository.save(newUser);

    }
}
