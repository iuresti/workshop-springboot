package org.desarrolladorslp.workshops.springboot.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.desarrolladorslp.workshops.springboot.exceptions.ResourceNotFoundForUserException;
import org.desarrolladorslp.workshops.springboot.forms.CardForm;
import org.desarrolladorslp.workshops.springboot.models.Card;
import org.desarrolladorslp.workshops.springboot.models.Column;
import org.desarrolladorslp.workshops.springboot.models.User;
import org.desarrolladorslp.workshops.springboot.services.CardService;
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
public class CardControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private CardService cardService;

    @Mock
    private UserService userService;

    @Mock
    private Principal principal;

    @InjectMocks
    private CardController cardController;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(cardController)
                .setControllerAdvice(new ErrorHandler())
                .build();
    }

    @Test
    public void givenValidForm_whenCreateCard_thenAcceptAnd200Status() throws Exception {
        // given
        CardForm cardForm = new CardForm();
        cardForm.setDescription("Card01");
        cardForm.setColumnId(1L);

        User user01 = new User().builder().id(1L).username("user01").build();

        Card dbCard01 = new Card();
        dbCard01.setDescription("Card01");
        dbCard01.setId(1L);

        CardForm toPersistForm = new CardForm();
        toPersistForm.setColumnId(1L);
        toPersistForm.setDescription("Card01");

        given(principal.getName()).willReturn(user01.getUsername());
        given(userService.findByUsername("user01")).willReturn(user01);
        given(cardService.createCardForUser(any(CardForm.class), anyLong())).willReturn(dbCard01);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/card")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(cardForm))
                        .principal(principal)
        ).andReturn().getResponse();

        // then
        verify(principal, atLeast(1)).getName();
        verify(userService).findByUsername("user01");
        verify(cardService).createCardForUser(toPersistForm, 1L);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(dbCard01));

    }

    @Test
    public void givenInvalidForm_whenCreateCard_thenRejectAnd400Status() throws Exception {
        // given
        CardForm cardForm = new CardForm();

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/card")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(cardForm))
                        .principal(principal)
        ).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    public void givenCardsForUserInStorage_whenRetrieveCardsForCurrentUser_thenGetDataAnd200Status()
            throws Exception {
        // given
        User user01 = new User().builder().id(1L).username("user01").build();

        Card card01 = new Card();
        card01.setDescription("Card01");
        card01.setId(1L);

        Card card02 = new Card();
        card02.setDescription("Card02");
        card02.setId(2L);

        List<Card> userCards = Arrays.asList(card01, card02);

        given(principal.getName()).willReturn(user01.getUsername());
        given(userService.findByUsername("user01")).willReturn(user01);
        given(cardService.findCardsByColumnForUser(1L, 1L)).willReturn(userCards);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/card/column/{columnId}", 1L)
                        .principal(principal)
        ).andReturn().getResponse();

        // then
        verify(principal, atLeast(1)).getName();
        verify(userService).findByUsername("user01");
        verify(cardService).findCardsByColumnForUser(1L, user01.getId());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(userCards));

    }

    @Test
    public void givenNoCardsForUserInStorage_whenRetrieveCardsForCurrentUser_thenGetNoDataAnd200Status()
            throws Exception {
        // given
        User user01 = new User().builder().id(1L).username("user01").build();

        given(principal.getName()).willReturn(user01.getUsername());
        given(userService.findByUsername("user01")).willReturn(user01);
        given(cardService.findCardsByColumnForUser(1L, user01.getId())).willReturn(Collections.emptyList());

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/card/column/{columnId}", 1L)
                        .principal(principal)
        ).andReturn().getResponse();

        // then
        verify(principal, atLeast(1)).getName();
        verify(userService).findByUsername("user01");
        verify(cardService).findCardsByColumnForUser(1L, user01.getId());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(Collections.emptyList()));
    }

    @Test
    public void givenCardForUserInStorage_whenRetrieveCardForCurrentUser_thenGetDataAnd200Status()
            throws Exception {
        // given
        User user01 = new User().builder().id(1L).username("user01").build();

        Card card01 = new Card();
        card01.setId(1L);
        card01.setDescription("Card01");

        given(principal.getName()).willReturn(user01.getUsername());
        given(userService.findByUsername("user01")).willReturn(user01);
        given(cardService.findCardForUser(1L, 1L)).willReturn(card01);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/card/{id}", card01.getId())
                        .principal(principal)
        )
                .andReturn().getResponse();

        // then
        verify(principal, atLeast(1)).getName();
        verify(userService).findByUsername("user01");
        verify(cardService).findCardForUser(card01.getId(), user01.getId());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(card01));
    }

    @Test
    public void givenCardForOtherUserInStorage_whenRetrieveCardForCurrentUser_thenGetNoDataAnd409Status()
            throws Exception {
        // given
        User user01 = new User().builder().id(1L).username("user01").build();

        given(principal.getName()).willReturn(user01.getUsername());
        given(userService.findByUsername("user01")).willReturn(user01);
        given(cardService.findCardForUser(1L, 1L))
                .willThrow(new ResourceNotFoundForUserException(
                        String.format("Card #%d not found for User #%d", 1L, 1L)));

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/card/{id}", 1L)
                        .principal(principal)
        ).andReturn().getResponse();

        // then
        verify(principal, atLeast(1)).getName();
        verify(userService).findByUsername("user01");
        verify(cardService).findCardForUser(1L, user01.getId());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());

    }

    @Test
    public void givenCardInStorage_whenDeleteBoardForCurrentUser_thenAcceptAnd200Status()
            throws Exception {
        // given
        User user01 = new User().builder().id(1L).username("user01").build();

        Card card01 = new Card();
        card01.setId(1L);
        card01.setDescription("Card01");

        given(principal.getName()).willReturn(user01.getUsername());
        given(userService.findByUsername("user01")).willReturn(user01);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/card/{id}", card01.getId())
                        .principal(principal)
        )
                .andReturn().getResponse();

        // then
        verify(principal, atLeast(1)).getName();
        verify(userService).findByUsername("user01");
        verify(cardService).deleteCardForUser(card01.getId(), user01.getId());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

    }

    @Test
    public void givenNoCardInStorage_whenDeleteBoardForCurrentUser_thenAcceptAnd409Status()
            throws Exception {
        // given
        User user01 = new User().builder().id(1L).username("user01").build();

        given(principal.getName()).willReturn(user01.getUsername());
        given(userService.findByUsername("user01")).willReturn(user01);

        willThrow(new ResourceNotFoundForUserException(String.format(
                "Card #%d not found for User #%d", 1L, 1L)))
                .given(cardService)
                .deleteCardForUser(1L, user01.getId());

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/card/{id}", 1L)
                        .principal(principal)
        ).andReturn().getResponse();

        // then
        verify(principal, atLeast(1)).getName();
        verify(userService).findByUsername("user01");
        verify(cardService).deleteCardForUser(1L, user01.getId());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    public void givenInvalidForm_whenUpdateCardForCurrentUser_thenRejectAnd400Status()
            throws Exception {
        // given
        CardForm cardForm = new CardForm();

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.put("/api/card")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(cardForm))
                        .principal(principal)
        )
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void givenBoardInStorageAndValidForm_whenUpdateCardForCurrentUser_thenAcceptAnd200Status()
            throws Exception {
        // given
        CardForm cardForm = new CardForm();
        cardForm.setColumnId(1L);
        cardForm.setDescription("Card01");
        cardForm.setId(1L);

        User user01 = new User().builder().id(1L).username("user01").build();

        // This is what is retrieved prior to update
        Card dbCard01 = new Card();
        dbCard01.setDescription("Card01");
        dbCard01.setId(1L);

        // This is what is expected after the update
        Card updatedCard01 = new Card();
        updatedCard01.setDescription("updatedCard01");
        updatedCard01.setId(1L);

        given(principal.getName()).willReturn(user01.getUsername());
        given(userService.findByUsername("user01")).willReturn(user01);
        given(cardService.updateCardForUser(cardForm, user01.getId())).willReturn(updatedCard01);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.put("/api/card")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(cardForm))
                        .principal(principal)
        )
                .andReturn().getResponse();

        // then
        verify(principal, atLeast(1)).getName();
        verify(userService).findByUsername("user01");
        verify(cardService).updateCardForUser(cardForm, user01.getId());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(updatedCard01));

    }

    @Test
    public void givenNoBoardForUserInStorage_whenUpdateCardForCurrentUser_thenAcceptAnd409Status()
            throws Exception {
        // given
        CardForm cardForm = new CardForm();
        cardForm.setColumnId(1L);
        cardForm.setDescription("Card01");
        cardForm.setId(1L);

        User user01 = new User().builder().id(1L).username("user01").build();

        given(principal.getName()).willReturn(user01.getUsername());
        given(userService.findByUsername("user01")).willReturn(user01);
        given(cardService.updateCardForUser(cardForm, user01.getId()))
                .willThrow(new ResourceNotFoundForUserException(
                        String.format("Card #%d not found for User #%d", 1L, 1L)));

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.put("/api/card")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(cardForm))
                        .principal(principal)
        ).andReturn().getResponse();

        // then
        verify(principal, atLeast(1)).getName();
        verify(userService).findByUsername("user01");
        verify(cardService).updateCardForUser(cardForm, user01.getId());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    public void givenBoardsInStorage_whenMoveCardForCurrentUser_thenAcceptAnd200Status()
            throws Exception {
        // given
        User user01 = new User().builder().id(1L).username("user01").build();

        Column targetColumn = new Column();
        targetColumn.setId(2L);

        given(principal.getName()).willReturn(user01.getUsername());
        given(userService.findByUsername("user01")).willReturn(user01);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.put("/api/card/move/{idCard}/{idColumnTarget}",
                        1L, targetColumn.getId())
                        .principal(principal)
        )
                .andReturn().getResponse();

        // then
        verify(principal, atLeast(1)).getName();
        verify(userService).findByUsername("user01");
        verify(cardService).moveCardForUser(1L, targetColumn.getId(), user01.getId());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void givenBoardsInStorage_whenMoveCardForCurrentUserToInvalidBoard_thenRejectAnd409Status()
            throws Exception {
        // given
        User user01 = new User().builder().id(1L).username("user01").build();

        Column invalidTargetColumn = new Column();
        invalidTargetColumn.setId(2L);

        given(principal.getName()).willReturn(user01.getUsername());
        given(userService.findByUsername("user01")).willReturn(user01);

        willThrow(new ResourceNotFoundForUserException(String.format(
                "Column #%d not found for User #%d", 2L, 1L)))
                .given(cardService)
                .moveCardForUser(1L, invalidTargetColumn.getId(), 1L);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.put("/api/card/move/{idCard}/{idColumnTarget}",
                        1L, invalidTargetColumn.getId())
                        .principal(principal)
        )
                .andReturn().getResponse();

        // then
        verify(principal, atLeast(1)).getName();
        verify(userService).findByUsername("user01");
        verify(cardService).moveCardForUser(1L, invalidTargetColumn.getId(), user01.getId());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
    }

}
