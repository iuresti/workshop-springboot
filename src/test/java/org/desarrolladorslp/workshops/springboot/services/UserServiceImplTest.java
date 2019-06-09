package org.desarrolladorslp.workshops.springboot.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.desarrolladorslp.workshops.springboot.exceptions.EmailAlreadyRegisteredException;
import org.desarrolladorslp.workshops.springboot.exceptions.InternalServerException;
import org.desarrolladorslp.workshops.springboot.exceptions.UsernameAlreadyRegisteredException;
import org.desarrolladorslp.workshops.springboot.forms.RegistrationForm;
import org.desarrolladorslp.workshops.springboot.models.Role;
import org.desarrolladorslp.workshops.springboot.models.RoleName;
import org.desarrolladorslp.workshops.springboot.models.User;
import org.desarrolladorslp.workshops.springboot.repository.RoleRepository;
import org.desarrolladorslp.workshops.springboot.repository.UserRepository;
import org.desarrolladorslp.workshops.springboot.services.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserServiceImplTest {

    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        userService = new UserServiceImpl(
                userRepository, roleRepository, passwordEncoder);
    }

    @Test
    public void givenUserInStorage_whenFindByValidUsername_thenRetrieveUser() {
        // given
        User user01 = new User().builder()
                .email("user01@example.com")
                .username("user01")
                .name("user01")
                .enabled(true)
                .build();

        given(userRepository.findByUsername("user01")).willReturn(Optional.of(user01));

        // when
        User serviceUser01 = userService.findByUsername("user01");

        // then
        assertNotNull(serviceUser01);
        assertTrue(user01.getUsername().equals(serviceUser01.getUsername()));

    }

    @Test(expected = EntityNotFoundException.class)
    public void givenNoUserInStorage_whenFindByUsername_thenThrowENFException() {
        // given
        User user01 = new User().builder()
                .email("user01@example.com")
                .username("user01")
                .name("user01")
                .enabled(true)
                .build();

        given(userRepository.findByUsername("user01")).willReturn(Optional.of(user01));

        // when
        User serviceUser01 = userService.findByUsername("user02");

    }

    @Test(expected = IllegalArgumentException.class)
    public void givenEmptyRegistrationForm_whenCreateUser_thenThrowIAException() {
        // given
        RegistrationForm registrationForm = new RegistrationForm();

        // when
        User serviceUser01 = userService.createUser(registrationForm);

    }

    @Test(expected = IllegalArgumentException.class)
    public void givenInvalidForm_whenCreateUser_thenThrowIAException() {
        // given
        RegistrationForm registrationForm = new RegistrationForm();
        registrationForm.setEmail("user01@example.com");
        registrationForm.setName("John Sanders");
        registrationForm.setPassword("password01");
        registrationForm.setUsername(null);

        // when
        User serviceUser01 = userService.createUser(registrationForm);
    }

    @Test(expected = EmailAlreadyRegisteredException.class)
    public void givenUserEmailInStorage_whenCreateUser_thenThrowEARException() {
        // given
        RegistrationForm registrationForm = new RegistrationForm();
        registrationForm.setEmail("user01@example.com");
        registrationForm.setName("John Sanders");
        registrationForm.setPassword("password01");
        registrationForm.setUsername("user01");

        User user01 = new User().builder()
                .email("user01@example.com")
                .username("user01")
                .name("user01")
                .enabled(true)
                .build();

        given(userRepository.findByEmail("user01@example.com")).willReturn(Optional.of(user01));

        // when
        User serviceUser01 = userService.createUser(registrationForm);

    }

    @Test(expected = UsernameAlreadyRegisteredException.class)
    public void givenUserNameInStorage_whenCreateUser_thenThrowUARException() {
        // given
        RegistrationForm registrationForm = new RegistrationForm();
        registrationForm.setEmail("user01@example.com");
        registrationForm.setName("John Sanders");
        registrationForm.setPassword("password01");
        registrationForm.setUsername("user01");

        User user01 = new User().builder()
                .email("user01@example.com")
                .username("user01")
                .name("user01")
                .enabled(true)
                .build();

        given(userRepository.findByEmail("user01@example.com")).willReturn(Optional.empty());
        given(userRepository.findByUsername("user01")).willReturn(Optional.of(user01));

        // when
        User serviceUser01 = userService.createUser(registrationForm);
    }

    @Test(expected = InternalServerException.class)
    public void givenNoUserRoleInStorage_whenCreateUser_thenThrowISException() {
        // given
        RegistrationForm registrationForm = new RegistrationForm();
        registrationForm.setEmail("user01@example.com");
        registrationForm.setName("John Sanders");
        registrationForm.setPassword("password01");
        registrationForm.setUsername("user01");

        given(userRepository.findByEmail("user01@example.com")).willReturn(Optional.empty());
        given(userRepository.findByUsername("user01")).willReturn(Optional.empty());
        given(roleRepository.findByName(RoleName.ROLE_USER)).willReturn(Optional.empty());

        // when
        User serviceUser01 = userService.createUser(registrationForm);

    }

    @Test
    public void givenValidRegistrationForm_whenCreateUser_thenCreateUserInStorage() {
        // given
        final String HASHED_PASSWORD = "$2a$10$fNsiLz3EMcHs1UNPJIZDQegwNu6Gkita23NkweqdlK7GslBBCwzl2";

        RegistrationForm registrationForm = new RegistrationForm();
        registrationForm.setEmail("user01@example.com");
        registrationForm.setName("John Sanders");
        registrationForm.setPassword("password01");
        registrationForm.setUsername("user01");

        Role roleUser = new Role(1L, RoleName.ROLE_USER, "User role");
        Set<Role> rolesList = new HashSet<>(); rolesList.add(roleUser);

        User savedUser = User.builder()
                .id(1L)
                .email(registrationForm.getEmail().toLowerCase())
                .username(registrationForm.getUsername().toLowerCase())
                .name(registrationForm.getName().toLowerCase())
                .password(HASHED_PASSWORD)
                .enabled(true)
                .authorities(rolesList)
                .build();

        given(userRepository.findByEmail("user01@example.com")).willReturn(Optional.empty());
        given(userRepository.findByUsername("user01")).willReturn(Optional.empty());
        given(roleRepository.findByName(RoleName.ROLE_USER)).willReturn(Optional.of(roleUser));
        given(passwordEncoder.encode(anyString()))
                .willReturn(HASHED_PASSWORD);
        given(userRepository.save(any(User.class))).willReturn(savedUser);

        // when
        User serviceUser01 = userService.createUser(registrationForm);

        // then
        assertNotNull(serviceUser01);
        assertTrue(serviceUser01.getId().equals(1L));
        assertTrue(serviceUser01.getEmail().equals(savedUser.getEmail()));
        assertTrue(serviceUser01.getEmail().equals(savedUser.getEmail()));
        assertTrue(serviceUser01.getUsername().equals(savedUser.getUsername()));
        assertTrue(serviceUser01.getPassword().equals(HASHED_PASSWORD));
        assertTrue(serviceUser01.getAuthorities().equals(savedUser.getAuthorities()));

    }

}
