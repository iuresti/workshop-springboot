package org.desarrolladorslp.workshops.springboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.desarrolladorslp.workshops.springboot.models.User;
import org.desarrolladorslp.workshops.springboot.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.persistence.EntityNotFoundException;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new ErrorHandler())
                .build();
    }

    @Test
    public void givenValidForm_whenCreateUser_thenAcceptAnd200Status() throws Exception {
        // given
        User user01 = new User().builder()
                .email("user01@example.com")
                .username("user01")
                .name("user01")
                .enabled(true)
                .build();

        User savedUser01 = new User().builder()
                .id(1L)
                .email("user01@example.com")
                .username("user01")
                .name("user01")
                .enabled(true)
                .build();

        given(userService.saveUser(user01)).willReturn(savedUser01);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(user01)))
                .andReturn().getResponse();

        // then
        verify(userService).saveUser(user01);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    public void givenEmailInStorage_whenRetrieveUserByEmail_thenGetDataAnd200Status()
            throws Exception {
        // given
        final String EMAIL = "user01@example.com";

        User user01 = new User().builder()
                .id(1L)
                .email("user01@example.com")
                .username("user01")
                .name("user01")
                .enabled(true)
                .build();

        given(userService.findByEmail(EMAIL)).willReturn(user01);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/user/email/{email}/", EMAIL))
                .andReturn().getResponse();

        // then
        verify(userService).findByEmail(EMAIL);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(user01));

    }

    @Test
    public void givenNoEmailInStorage_whenRetrieveUserByEmail_thenAcceptAnd404Status()
            throws Exception {
        // given
        final String EMAIL = "user01@example.com";

        given(userService.findByEmail(EMAIL)).willThrow(new EntityNotFoundException("User not found"));

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/user/email/{email}/", EMAIL))
                .andReturn().getResponse();

        // then
        verify(userService).findByEmail(EMAIL);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());

    }

    @Test
    public void givenUserIdInStorage_whenRetrieveUserById_thenGetDataAnd200Status()
            throws Exception {
        // given
        final Long ID = 1L;

        User user01 = new User().builder()
                .id(1L)
                .email("user01@example.com")
                .username("user01")
                .name("user01")
                .enabled(true)
                .build();

        given(userService.findById(ID)).willReturn(user01);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/user/{id}", ID))
                .andReturn().getResponse();

        // then
        verify(userService).findById(ID);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(user01));

    }

    @Test
    public void givenNoUserIdInStorage_whenRetrieveUserById_thenAcceptAnd404Status()
            throws Exception {
        // given
        final Long ID = 1L;

        given(userService.findById(ID)).willThrow(new EntityNotFoundException("User not found"));

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/user/{id}", ID))
                .andReturn().getResponse();

        // then
        verify(userService).findById(ID);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());

    }

    @Test
    public void givenUserInStorage_whenDeleteUser_thenAcceptAnd200Status() throws Exception {
        // given
        final Long ID = 1L;

        willDoNothing().given(userService).deleteById(ID);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/user/{id}", ID))
                .andReturn().getResponse();

        // then
        verify(userService).deleteById(ID);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void givenNoUserInStorage_whenDeleteUser_thenAcceptAnd404Status() throws Exception {
        // given
        final Long ID = 1L;

        willThrow(new EntityNotFoundException("User not found")).given(userService).deleteById(ID);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/user/{id}", ID))
                .andReturn().getResponse();

        // then
        verify(userService).deleteById(ID);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void givenUserInStorage_whenUpdateUser_thenAcceptAnd200Status() throws Exception {
        // given
        User newUser01 = new User().builder()
                .id(1L)
                .email("newUser01@example.com")
                .username("newUser01")
                .name("newUser01")
                .enabled(true)
                .build();

        given(userService.updateUser(newUser01)).willReturn(newUser01);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.put("/api/user")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(newUser01)))
                .andReturn().getResponse();

        // then
        verify(userService).updateUser(newUser01);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void givenUsersInStorage_WhenRetrieveAllUsers_thenGetDataAnd200Status()
            throws Exception {
        // given
        User user01 = new User().builder()
                .id(1L)
                .email("user01@example.com")
                .username("user01")
                .name("user01")
                .enabled(true)
                .build();

        given(userService.getAll()).willReturn(Arrays.asList(user01));

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/user")
        )
                .andReturn().getResponse();

        // then
        verify(userService).getAll();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(Arrays.asList(user01)));
    }

}
