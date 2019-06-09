package org.desarrolladorslp.workshops.springboot.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import org.desarrolladorslp.workshops.springboot.forms.RegistrationForm;
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

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class AccountControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private AccountController accountController;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }


    @Test
    public void givenInvalidForm_whenRegisterUser_thenRejectAnd400Status() throws Exception {
        // given
        RegistrationForm registrationForm = new RegistrationForm();
        registrationForm.setName("John Sanders");
        registrationForm.setUsername("user01");
        registrationForm.setEmail("user01@example.com");
        registrationForm.setPassword(null);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/register")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(objectMapper.writeValueAsString(registrationForm)))
                    .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    public void givenValidForm_whenRegisterUser_thenAcceptAnd200Status() throws Exception {
        // given
        RegistrationForm registrationForm = new RegistrationForm();
        registrationForm.setName("John Sanders");
        registrationForm.setUsername("user01");
        registrationForm.setEmail("user01@example.com");
        registrationForm.setPassword("s3cr3tp4ss123");

        User user01 = new User().builder()
                .email("user01@example.com")
                .username("user01")
                .name("John Sanders")
                .password("s3cr3tp4ss123")
                .enabled(true)
                .build();

        given(userService.createUser(any(RegistrationForm.class))).willReturn(user01);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(registrationForm))
        ).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEmpty();
    }

}
