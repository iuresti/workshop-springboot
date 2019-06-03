package org.desarrolladorslp.workshops.springboot.services;

import org.desarrolladorslp.workshops.springboot.forms.BoardForm;
import org.desarrolladorslp.workshops.springboot.models.Board;
import org.desarrolladorslp.workshops.springboot.models.User;
import org.desarrolladorslp.workshops.springboot.repository.BoardRepository;
import org.desarrolladorslp.workshops.springboot.repository.CardRepository;
import org.desarrolladorslp.workshops.springboot.repository.ColumnRepository;
import org.desarrolladorslp.workshops.springboot.repository.UserRepository;
import org.desarrolladorslp.workshops.springboot.services.impl.BoardServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class BoardServiceImplTest {

    private BoardServiceImpl boardService;

    @Mock
    private BoardRepository boardRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ColumnRepository columnRepository;
    @Mock
    private CardRepository cardRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        boardService = new BoardServiceImpl(
                boardRepository, userRepository, columnRepository, cardRepository);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenInvalidForm_whenCreateUser_thenThrowIAException() {
        // given
        BoardForm boardForm = new BoardForm(1L, "TODO", null);

        // when
        Board serviceBoard01 = boardService.create(boardForm);

    }

    @Test(expected = EntityNotFoundException.class)
    public void givenValidFormWithInvalidUserId_whenCreateUser_thenThrowENFException() {
        // given
        BoardForm boardForm = new BoardForm(1L, "TODO", 2L);

        User user01 = new User().builder()
                .id(1L)
                .email("user01@example.com")
                .username("user01")
                .name("user01")
                .enabled(true)
                .build();

        given(userRepository.findById(1L)).willReturn(Optional.of(user01));

        // when
        Board serviceBoard01 = boardService.create(boardForm);

    }

    @Test
    public void givenValidBoardForm_whenCreateBoard_thenCreateBoardInStorage() {
        // given
        BoardForm boardForm = new BoardForm(1L, "TODO", 1L);

        User user01 = new User().builder()
                .id(1L)
                .email("user01@example.com")
                .username("user01")
                .name("user01")
                .enabled(true)
                .build();

        Board board01 = new Board();
        board01.setId(1L);
        board01.setName("TODO");
        board01.setUser(user01);

        given(userRepository.findById(1L)).willReturn(Optional.of(user01));
        given(boardRepository.save(any(Board.class))).willReturn(board01);

        // when
        Board serviceBoard01 = boardService.create(boardForm);

        // then
        assertNotNull(serviceBoard01);
        assertTrue(board01.getId().equals(serviceBoard01.getId()));
        assertTrue(board01.getName().equals(serviceBoard01.getName()));
        assertTrue(board01.getUser().getId().equals(serviceBoard01.getUser().getId()));

    }

    @Test
    public void givenBoardInStorage_whenFindByIdAndUserId_thenRetrieveBoard() {
        // given
        User user01 = new User().builder()
                .id(1L)
                .email("user01@example.com")
                .username("user01")
                .name("user01")
                .enabled(true)
                .build();

        Board board01 = new Board();
        board01.setId(1L);
        board01.setName("TODO");
        board01.setUser(user01);

        given(boardRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(board01));

        // when
        Board serviceBoard01 = boardService.findByIdAndUserId(1L, 1L);

        // then
        assertNotNull(serviceBoard01);
        assertTrue(board01.getId().equals(serviceBoard01.getId()));
        assertTrue(board01.getName().equals(serviceBoard01.getName()));
        assertTrue(board01.getUser().equals(serviceBoard01.getUser()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void givenBoardInStorage_whenFindByInvalidIdAndUserId_thenThrowENFException() {
        // given
        User user01 = new User().builder()
                .id(1L)
                .email("user01@example.com")
                .username("user01")
                .name("user01")
                .enabled(true)
                .build();

        Board board01 = new Board();
        board01.setId(1L);
        board01.setName("TODO");
        board01.setUser(user01);

        given(boardRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(board01));

        // when
        Board serviceBoard01 = boardService.findByIdAndUserId(2L, 1L);
    }

    @Test(expected = EntityNotFoundException.class)
    public void givenBoardInStorage_whenFindByIdAndInvalidUserId_thenThrowENFException() {
        // given
        User user01 = new User().builder()
                .id(1L)
                .email("user01@example.com")
                .username("user01")
                .name("user01")
                .enabled(true)
                .build();

        Board board01 = new Board();
        board01.setId(1L);
        board01.setName("TODO");
        board01.setUser(user01);

        given(boardRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(board01));

        // when
        Board serviceBoard01 = boardService.findByIdAndUserId(1L, 2L);
    }

    @Test
    public void givenValidFormAndBoardInStorage_whenUpdateBoard_thenUpdateBoardInStorage() {
        // given
        BoardForm boardForm = new BoardForm(1L, "Traveling", 1L);

        User user01 = new User().builder()
                .id(1L)
                .email("user01@example.com")
                .username("user01")
                .name("user01")
                .enabled(true)
                .build();

        Board board01 = new Board();
        board01.setId(1L);
        board01.setName("TODO");
        board01.setUser(user01);

        Board updatedBoard01 = new Board();
        updatedBoard01.setId(1L);
        updatedBoard01.setName("Traveling");
        updatedBoard01.setUser(user01);

        given(boardRepository.findById(1L)).willReturn(Optional.of(board01));
        given(boardRepository.save(any(Board.class))).willReturn(updatedBoard01);

        // when
        Board serviceBoard01 = boardService.update(boardForm);

        // then
        verify(boardRepository).findById(1L);
        verify(boardRepository).save(updatedBoard01);

        assertNotNull(serviceBoard01);
        assertTrue(updatedBoard01.getId().equals(serviceBoard01.getId()));
        assertTrue(updatedBoard01.getName().equals(serviceBoard01.getName()));
        assertTrue(updatedBoard01.getUser().getId().equals(serviceBoard01.getUser().getId()));

    }

}
