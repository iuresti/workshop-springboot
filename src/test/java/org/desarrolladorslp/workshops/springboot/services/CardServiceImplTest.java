package org.desarrolladorslp.workshops.springboot.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.desarrolladorslp.workshops.springboot.exceptions.ResourceNotFoundForUserException;
import org.desarrolladorslp.workshops.springboot.forms.CardForm;
import org.desarrolladorslp.workshops.springboot.models.Board;
import org.desarrolladorslp.workshops.springboot.models.Card;
import org.desarrolladorslp.workshops.springboot.models.Column;
import org.desarrolladorslp.workshops.springboot.models.User;
import org.desarrolladorslp.workshops.springboot.repository.CardRepository;
import org.desarrolladorslp.workshops.springboot.repository.ColumnRepository;
import org.desarrolladorslp.workshops.springboot.repository.UserRepository;
import org.desarrolladorslp.workshops.springboot.services.impl.CardServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CardServiceImplTest {

    private CardServiceImpl cardService;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private ColumnRepository columnRepository;

    @Mock
    private UserRepository userRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        cardService = new CardServiceImpl(
                cardRepository, columnRepository, userRepository);
    }

    @Test
    public void givenCardInStorage_whenFindCardById_thenRetrieveCard() {
        // given
        Card card01 = new Card();
        card01.setColumn(null);
        card01.setDescription("Description");
        card01.setId(1L);

        given(cardRepository.findById(1L)).willReturn(Optional.of(card01));

        // when
        Card dbCard01 = cardService.findById(1L);

        // then
        assertNotNull(dbCard01);
        assertEquals(card01, dbCard01);
    }

    @Test(expected = EntityNotFoundException.class)
    public void givenNoCardInStorage_whenFindCardById_thenThrowENFException() {
        // given
        given(cardRepository.findById(2L)).willThrow(
                new EntityNotFoundException(String.format("Card #%s not found", 2L)));

        // when
        Card dbCard01 = cardService.findById(2L);
    }

    @Test
    public void givenCardForUserInStorage_whenCheckExistByIdAndUserId_thenIsTrue() {
        // given
        User user01 = new User();
        user01.setUsername("user01");
        user01.setId(1L);

        Board board01 = new Board();
        board01.setName("Board01");
        board01.setUser(user01);
        board01.setId(1L);

        Column column01 = new Column();
        column01.setBoard(board01);
        column01.setName("Column01");
        column01.setId(1L);

        Card card01 = new Card();
        card01.setColumn(column01);
        card01.setDescription("Description");
        card01.setId(1L);

        given(cardRepository.findById(1L)).willReturn(Optional.of(card01));
        given(columnRepository.findById(1L)).willReturn(Optional.of(column01));
        given(userRepository.findById(1L)).willReturn(Optional.of(user01));
        given(columnRepository.existsByIdAndUserId(1L, 1L)).willReturn(true);

        // when
        boolean result = cardService.existsCardForUser(1L, 1L);

        // then
        assertTrue(result);
    }

    @Test
    public void givenNoCardForUserInStorage_whenCheckExistByIdAndUserId_thenIsFalse() {
        // given
        User user01 = new User();
        user01.setUsername("user01");
        user01.setId(1L);

        Board board01 = new Board();
        board01.setName("Board01");
        board01.setId(1L);

        Column column01 = new Column();
        column01.setBoard(board01);
        column01.setName("Column01");
        column01.setId(1L);

        Card card01 = new Card();
        card01.setColumn(column01);
        card01.setDescription("Description");
        card01.setId(1L);

        given(cardRepository.findById(1L)).willReturn(Optional.of(card01));
        given(columnRepository.findById(1L)).willReturn(Optional.of(column01));
        given(userRepository.findById(1L)).willReturn(Optional.of(user01));
        given(columnRepository.existsByIdAndUserId(1L, 1L)).willReturn(false);

        // when
        boolean result = cardService.existsCardForUser(1L, 1L);

        // then
        assertFalse(result);

    }

    @Test(expected = ResourceNotFoundForUserException.class)
    public void givenNoColumnForUserInStorage_whenCreateCard_thenThrowRNFException() {
        // given
        CardForm cardForm = new CardForm();
        cardForm.setColumnId(1L);
        cardForm.setDescription("Card01");

        User user01 = new User();
        user01.setUsername("user01");
        user01.setId(1L);

        Board board01 = new Board();
        board01.setName("Board01");
        board01.setId(1L);

        Column column01 = new Column();
        column01.setId(1L);
        column01.setName("Column01");
        column01.setBoard(board01);

        given(columnRepository.findById(1L)).willReturn(Optional.of(column01));
        given(userRepository.findById(1L)).willReturn(Optional.of(user01));
        given(columnRepository.existsByIdAndUserId(1L, 1L)).willReturn(false);

        // when
        Card dbCard = cardService.createCardForUser(cardForm, 1L);
    }

    @Test
    public void givenColumnForUserInStorage_whenCreateCard_thenCreateCardInStorage() {
        // given
        CardForm cardForm = new CardForm();
        cardForm.setColumnId(1L);
        cardForm.setDescription("Card01");

        User user01 = new User();
        user01.setUsername("user01");
        user01.setId(1L);

        Board board01 = new Board();
        board01.setName("Board01");
        board01.setUser(user01);
        board01.setId(1L);

        Column column01 = new Column();
        column01.setId(1L);
        column01.setName("Column01");
        column01.setBoard(board01);

        Card toCreateCard01 = new Card();
        toCreateCard01.setDescription("Card01");
        toCreateCard01.setColumn(column01);

        Card dbCard01 = new Card();
        dbCard01.setDescription("Card01");
        dbCard01.setColumn(column01);
        dbCard01.setId(1L);

        given(columnRepository.findById(1L)).willReturn(Optional.of(column01));
        given(userRepository.findById(1L)).willReturn(Optional.of(user01));
        given(columnRepository.existsByIdAndUserId(1L, 1L)).willReturn(true);
        given(cardRepository.save(any(Card.class))).willReturn(dbCard01);

        // when
        Card serviceCard01 = cardService.createCardForUser(cardForm, 1L);

        // then
        verify(cardRepository).save(toCreateCard01);

        assertNotNull(serviceCard01);
        assertEquals(serviceCard01, dbCard01);
    }

    @Test
    public void givenColumnForUserInStorage_whenFindCardsByColumnForUser_thenRetrieveColumnCards() {
        // given
        User user01 = new User();
        user01.setUsername("user01");
        user01.setId(1L);

        Board board01 = new Board();
        board01.setName("Board01");
        board01.setUser(user01);
        board01.setId(1L);

        Column column01 = new Column();
        column01.setBoard(board01);
        column01.setName("Column01");
        column01.setId(1L);

        Card card01 = new Card();
        card01.setColumn(column01);
        card01.setDescription("Card01");
        card01.setId(1L);

        Card card02 = new Card();
        card02.setColumn(column01);
        card02.setDescription("Card02");
        card02.setId(2L);

        given(columnRepository.findById(1L)).willReturn(Optional.of(column01));
        given(userRepository.findById(1L)).willReturn(Optional.of(user01));
        given(columnRepository.existsByIdAndUserId(1L, 1L)).willReturn(true);
        given(cardRepository.findByColumn(1L)).willReturn(Arrays.asList(card01, card02));

        // when
        List<Card> dbCards = cardService.findCardsByColumnForUser(1L, 1L);

        // then
        assertNotNull(dbCards);
        assertTrue(dbCards.size() == 2);
        assertEquals(dbCards, Arrays.asList(card01, card02));

    }

    @Test(expected = ResourceNotFoundForUserException.class)
    public void givenNoColumnForUserInStorage_whenFindCardsByColumnForUser_thenThrowRNFException() {
        // given
        User user01 = new User();
        user01.setUsername("user01");
        user01.setId(1L);

        Board board01 = new Board();
        board01.setName("Board01");
        board01.setId(1L);

        Column column01 = new Column();
        column01.setBoard(board01);
        column01.setName("Column01");
        column01.setId(1L);

        Card card01 = new Card();
        card01.setColumn(column01);
        card01.setDescription("Card01");
        card01.setId(1L);

        given(columnRepository.findById(1L)).willReturn(Optional.of(column01));
        given(userRepository.findById(1L)).willReturn(Optional.of(user01));
        given(columnRepository.existsByIdAndUserId(1L, 1L)).willReturn(false);

        // when
        List<Card> dbCards = cardService.findCardsByColumnForUser(1L, 1L);
    }

    @Test
    public void givenCardForUserInStorage_whenFindCardForUser_thenRetrieveCard() {
        // given
        User user01 = new User();
        user01.setUsername("user01");
        user01.setId(1L);

        Board board01 = new Board();
        board01.setName("Board01");
        board01.setUser(user01);
        board01.setId(1L);

        Column column01 = new Column();
        column01.setBoard(board01);
        column01.setName("Column01");
        column01.setId(1L);

        Card card01 = new Card();
        card01.setColumn(column01);
        card01.setDescription("Card01");
        card01.setId(1L);

        given(columnRepository.findById(1L)).willReturn(Optional.of(column01));
        given(userRepository.findById(1L)).willReturn(Optional.of(user01));
        given(columnRepository.existsByIdAndUserId(1L, 1L)).willReturn(true);
        given(cardRepository.findById(1L)).willReturn(Optional.of(card01));

        // when
        Card dbCard01 = cardService.findCardForUser(1L, 1L);

        // then
        assertNotNull(dbCard01);
        assertEquals(dbCard01, card01);
    }

    @Test(expected = ResourceNotFoundForUserException.class)
    public void givenNoCardForUserInStorage_whenFindCardForUser_thenThrowRNFException() {
        // given
        User user01 = new User();
        user01.setUsername("user01");
        user01.setId(1L);

        Board board01 = new Board();
        board01.setName("Board01");
        board01.setId(1L);

        Column column01 = new Column();
        column01.setBoard(board01);
        column01.setName("Column01");
        column01.setId(1L);

        Card card01 = new Card();
        card01.setColumn(column01);
        card01.setDescription("Card01");
        card01.setId(1L);

        given(cardRepository.findById(1L)).willReturn(Optional.of(card01));
        given(columnRepository.findById(1L)).willReturn(Optional.of(column01));
        given(userRepository.findById(1L)).willReturn(Optional.of(user01));
        given(columnRepository.existsByIdAndUserId(1L, 1L)).willReturn(false);

        // when
        Card dbCard01 = cardService.findCardForUser(1L, 1L);
    }

    @Test
    public void givenCardForUserInStorage_whenDeleteCard_thenDeleteCardFromStorage() {
        // given
        User user01 = new User();
        user01.setUsername("user01");
        user01.setId(1L);

        Board board01 = new Board();
        board01.setName("Board01");
        board01.setUser(user01);
        board01.setId(1L);

        Column column01 = new Column();
        column01.setBoard(board01);
        column01.setName("Column01");
        column01.setId(1L);

        Card card01 = new Card();
        card01.setColumn(column01);
        card01.setDescription("Card01");
        card01.setId(1L);

        given(columnRepository.findById(1L)).willReturn(Optional.of(column01));
        given(userRepository.findById(1L)).willReturn(Optional.of(user01));
        given(columnRepository.existsByIdAndUserId(1L, 1L)).willReturn(true);
        given(cardRepository.findById(1L)).willReturn(Optional.of(card01));

        // when
        cardService.deleteCardForUser(1L, 1L);

        // then
        verify(cardRepository).deleteById(1L);

    }

    @Test(expected = ResourceNotFoundForUserException.class)
    public void givenNoCardForUserInStorage_whenDeleteCard_thenThrowRNFException() {
        // given
        User user01 = new User();
        user01.setUsername("user01");
        user01.setId(1L);

        Board board01 = new Board();
        board01.setName("Board01");
        board01.setId(1L);

        Column column01 = new Column();
        column01.setBoard(board01);
        column01.setName("Column01");
        column01.setId(1L);

        Card card01 = new Card();
        card01.setColumn(column01);
        card01.setDescription("Card01");
        card01.setId(1L);

        given(cardRepository.findById(1L)).willReturn(Optional.of(card01));
        given(columnRepository.findById(1L)).willReturn(Optional.of(column01));
        given(userRepository.findById(1L)).willReturn(Optional.of(user01));
        given(columnRepository.existsByIdAndUserId(1L, 1L)).willReturn(false);

        // when
        cardService.deleteCardForUser(1L, 1L);
    }

    @Test
    public void givenCardForUserInStorageAndValidCardForm_whenUpdateCard_thenUpdateCardInStorage() {
        // given
        CardForm cardForm = new CardForm();
        cardForm.setId(1L);
        cardForm.setColumnId(1L);
        cardForm.setDescription("updatedCard01");

        User user01 = new User();
        user01.setUsername("user01");
        user01.setId(1L);

        Board board01 = new Board();
        board01.setName("Board01");
        board01.setUser(user01);
        board01.setId(1L);

        Column column01 = new Column();
        column01.setId(1L);
        column01.setName("Column01");
        column01.setBoard(board01);

        Card updatedDbCard01 = new Card();
        updatedDbCard01.setDescription("updatedCard01");
        updatedDbCard01.setColumn(column01);
        updatedDbCard01.setId(1L);

        Card dbCard01 = new Card();
        dbCard01.setDescription("Card01");
        dbCard01.setColumn(column01);
        dbCard01.setId(1L);

        given(columnRepository.findById(1L)).willReturn(Optional.of(column01));
        given(userRepository.findById(1L)).willReturn(Optional.of(user01));
        given(columnRepository.existsByIdAndUserId(1L, 1L)).willReturn(true);
        given(cardRepository.findById(1L)).willReturn(Optional.of(dbCard01));
        given(cardRepository.save(any(Card.class))).willReturn(updatedDbCard01);

        // when
        Card serviceUpdatedCard01 = cardService.updateCardForUser(cardForm, 1L);

        // then
        verify(cardRepository).save(updatedDbCard01);

        assertNotNull(serviceUpdatedCard01);
        assertEquals(serviceUpdatedCard01, updatedDbCard01);
    }

    @Test(expected = ResourceNotFoundForUserException.class)
    public void givenNoCardForUserInStorageAndValidCardForm_whenUpdateCard_thenThrowRNFException() {
        // given
        CardForm cardForm = new CardForm();
        cardForm.setId(1L);
        cardForm.setColumnId(1L);
        cardForm.setDescription("updatedCard01");

        User user01 = new User();
        user01.setUsername("user01");
        user01.setId(1L);

        Board board01 = new Board();
        board01.setName("Board01");
        board01.setUser(user01);
        board01.setId(1L);

        Column column01 = new Column();
        column01.setId(1L);
        column01.setName("Column01");
        column01.setBoard(board01);

        Card card01 = new Card();
        card01.setId(1L);
        card01.setColumn(column01);
        card01.setDescription("Card01");

        given(columnRepository.findById(1L)).willReturn(Optional.of(column01));
        given(userRepository.findById(1L)).willReturn(Optional.of(user01));
        given(columnRepository.existsByIdAndUserId(1L, 1L)).willReturn(false);
        given(cardRepository.findById(1L)).willReturn(Optional.of(card01));

        // when
        Card serviceUpdatedCard01 = cardService.updateCardForUser(cardForm, 1L);
    }

    @Test
    public void givenCardAndColumnForUserInStorage_whenMoveCard_thenMoveCardToNewColumn() {
        // given
        final long CARD_ID = 1L;
        final long TARGET_COLUMN_ID = 2;

        User user01 = new User();
        user01.setUsername("user01");
        user01.setId(1L);

        Board board01 = new Board();
        board01.setName("Board01");
        board01.setUser(user01);
        board01.setId(1L);

        Column sourceColumn = new Column();
        sourceColumn.setId(1L);
        sourceColumn.setName("Column01");
        sourceColumn.setBoard(board01);

        Column targetColumn = new Column();
        targetColumn.setId(2L);
        targetColumn.setName("Column02");
        targetColumn.setBoard(board01);

        Card dbCard01 = new Card();
        dbCard01.setId(1L);
        dbCard01.setColumn(sourceColumn);
        dbCard01.setDescription("Card01");

        Card updatedDbCard01 = new Card();
        updatedDbCard01.setId(1L);
        updatedDbCard01.setColumn(targetColumn);
        updatedDbCard01.setDescription("Card01");

        given(cardRepository.findById(1L)).willReturn(Optional.of(dbCard01));
        given(columnRepository.findById(1L)).willReturn(Optional.of(sourceColumn));
        given(columnRepository.findById(2L)).willReturn(Optional.of(targetColumn));
        given(userRepository.findById(1L)).willReturn(Optional.of(user01));
        given(columnRepository.existsByIdAndUserId(1L, 1L)).willReturn(true);
        given(columnRepository.existsByIdAndUserId(2L, 1L)).willReturn(true);

        // when
        cardService.moveCardForUser(CARD_ID, TARGET_COLUMN_ID, 1L);

        // then
        verify(cardRepository).save(updatedDbCard01);
    }

    @Test(expected = ResourceNotFoundForUserException.class)
    public void givenCardAndNoColumnForUserInStorage_whenMoveCard_thenThrowRNFException() {
        // given
        final long CARD_ID = 1L;
        final long TARGET_COLUMN_ID = 2;

        User user01 = new User();
        user01.setUsername("user01");
        user01.setId(1L);

        Board board01 = new Board();
        board01.setName("Board01");
        board01.setUser(user01);
        board01.setId(1L);

        Column sourceColumn = new Column();
        sourceColumn.setId(1L);
        sourceColumn.setName("Column01");
        sourceColumn.setBoard(board01);

        Column targetColumn = new Column();
        targetColumn.setId(2L);
        targetColumn.setName("Column02");

        Card dbCard01 = new Card();
        dbCard01.setId(1L);
        dbCard01.setColumn(sourceColumn);
        dbCard01.setDescription("Card01");

        Card updatedDbCard01 = new Card();
        updatedDbCard01.setId(1L);
        updatedDbCard01.setColumn(targetColumn);
        updatedDbCard01.setDescription("Card01");

        given(cardRepository.findById(1L)).willReturn(Optional.of(dbCard01));
        given(columnRepository.findById(1L)).willReturn(Optional.of(sourceColumn));
        given(columnRepository.findById(2L)).willReturn(Optional.of(targetColumn));
        given(userRepository.findById(1L)).willReturn(Optional.of(user01));
        given(columnRepository.existsByIdAndUserId(1L, 1L)).willReturn(true);
        given(columnRepository.existsByIdAndUserId(2L, 1L)).willReturn(false);

        // when
        cardService.moveCardForUser(CARD_ID, TARGET_COLUMN_ID, 1L);
    }

}
