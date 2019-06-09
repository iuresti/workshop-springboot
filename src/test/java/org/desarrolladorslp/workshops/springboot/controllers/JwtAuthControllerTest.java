package org.desarrolladorslp.workshops.springboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.desarrolladorslp.workshops.springboot.forms.LoginForm;
import org.desarrolladorslp.workshops.springboot.models.User;
import org.desarrolladorslp.workshops.springboot.security.jwt.JwtProperties;
import org.desarrolladorslp.workshops.springboot.security.jwt.JwtTokenVM;
import org.desarrolladorslp.workshops.springboot.security.jwt.TokenProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class JwtAuthControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private TokenProvider tokenProvider;
    @Mock
    private JwtProperties jwtProperties;

    @InjectMocks
    private JwtAuthController jwtAuthController;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(jwtAuthController).build();
    }

    @Test
    public void givenInvalidForm_whenAuthUser_thenRejectAnd400Status() throws Exception {
        // given
        LoginForm loginForm = new LoginForm();
        loginForm.setUsername("user01");

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/authenticate")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(objectMapper.writeValueAsString(loginForm))
        ).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    public void givenValidForm_whenAuthUser_thenGetJwtTokenAnd200Status() throws Exception {
        // given
        final String FAKE_TOKEN = "HEADER.PAYLOAD.SIGNATURE";
        final Long EXPIRE_LENGTH = 1800L;

        LoginForm loginForm = new LoginForm();
        loginForm.setUsername("user01");
        loginForm.setPassword("s3cr3tp4ss123");

        User user01 = new User().builder()
                .email("user01@example.com")
                .username("user01")
                .name("John Sanders")
                .enabled(true)
                .build();

        Authentication managerOutput = new UsernamePasswordAuthenticationToken(user01, FAKE_TOKEN);
        UsernamePasswordAuthenticationToken managerInput =
                new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword());

        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willReturn(managerOutput);
        given(tokenProvider.generateToken(any(User.class))).willReturn(FAKE_TOKEN);
        given(jwtProperties.getExpireLength()).willReturn(EXPIRE_LENGTH);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/authenticate")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(loginForm))
        ).andReturn().getResponse();

        // then
        verify(tokenProvider).generateToken(user01);
        verify(authenticationManager).authenticate(managerInput);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString().equals(new JwtTokenVM(FAKE_TOKEN, EXPIRE_LENGTH)));

    }


}
