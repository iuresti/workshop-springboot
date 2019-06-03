package org.desarrolladorslp.workshops.springboot.services;

import org.desarrolladorslp.workshops.springboot.exceptions.ResourceNotFoundForUserException;
import org.desarrolladorslp.workshops.springboot.forms.ColumnForm;
import org.desarrolladorslp.workshops.springboot.models.Board;
import org.desarrolladorslp.workshops.springboot.models.Column;
import org.desarrolladorslp.workshops.springboot.models.User;
import org.desarrolladorslp.workshops.springboot.repository.BoardRepository;
import org.desarrolladorslp.workshops.springboot.repository.ColumnRepository;
import org.desarrolladorslp.workshops.springboot.repository.UserRepository;
import org.desarrolladorslp.workshops.springboot.services.impl.ColumnServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class ColumnServiceImplTest {

    private ColumnServiceImpl columnService;

    @Mock
    private ColumnRepository columnRepository;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private UserRepository userRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        columnService = new ColumnServiceImpl(
                columnRepository, boardRepository, userRepository);
    }

    @Test
    public void it_should_retrieve_column_when_given_a_valid_column_id() {
        // given
        Column column01 = new Column();
        column01.setName("Column01");
        column01.setId(1L);

        given(columnRepository.findById(1L)).willReturn(Optional.of(column01));

        // when
        Column dbColumn01 = columnService.findById(1L);

        // then
        assertNotNull(dbColumn01);
        assertEquals(column01, dbColumn01);
    }

    @Test(expected = EntityNotFoundException.class)
    public void it_should_throw_not_found_exception_when_given_a_non_existent_column_id() {
        // given
        given(columnRepository.findById(2L)).willThrow(
                new EntityNotFoundException(String.format("Column #%s not found", 2L)));

        // when
        Column dbColumn01 = columnService.findById(2L);
    }

    @Test
    public void it_should_return_true_when_it_exists_a_column_for_given_user_id() {
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

        given(columnRepository.findById(1L)).willReturn(Optional.of(column01));
        given(boardRepository.findById(1L)).willReturn(Optional.of(board01));
        given(userRepository.findById(1L)).willReturn(Optional.of(user01));
        given(boardRepository.existsByIdAndUserId(1L, 1L)).willReturn(true);

        // when
        boolean result = columnService.existsColumnForUser(1L, 1L);

        // then
        assertTrue(result);
    }

    @Test
    public void it_should_return_false_when_it_does_not_exist_a_column_id_for_given_user_id() {
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

        given(columnRepository.findById(1L)).willReturn(Optional.of(column01));
        given(boardRepository.findById(1L)).willReturn(Optional.of(board01));
        given(userRepository.findById(1L)).willReturn(Optional.of(user01));
        given(boardRepository.existsByIdAndUserId(1L, 1L)).willReturn(false);

        // when
        boolean result = columnService.existsColumnForUser(1L, 1L);

        // then
        assertFalse(result);

    }

    @Test(expected = ResourceNotFoundForUserException.class)
    public void it_should_throw_an_exception_when_creating_a_new_column_with_a_non_associated_board_id() {
        // given
        ColumnForm columnForm = new ColumnForm();
        columnForm.setBoardId(1L);
        columnForm.setName("Column01");

        User user01 = new User();
        user01.setUsername("user01");
        user01.setId(1L);

        Board board01 = new Board();
        board01.setName("Board01");
        board01.setId(1L);

        given(boardRepository.findById(1L)).willReturn(Optional.of(board01));
        given(userRepository.findById(1L)).willReturn(Optional.of(user01));
        given(boardRepository.existsByIdAndUserId(1L, 1L)).willReturn(false);

        // when
        Column dbColumn = columnService.createColumnForUser(columnForm, 1L);
    }

    @Test
    public void it_should_create_a_column_when_given_an_associated_board_id() {
        // given
        ColumnForm columnForm = new ColumnForm();
        columnForm.setBoardId(1L);
        columnForm.setName("Column01");

        User user01 = new User();
        user01.setUsername("user01");
        user01.setId(1L);

        Board board01 = new Board();
        board01.setName("Board01");
        board01.setUser(user01);
        board01.setId(1L);

        Column toCreateColumn01 = new Column();
        toCreateColumn01.setName("Column01");
        toCreateColumn01.setBoard(board01);

        Column dbColumn01 = new Column();
        dbColumn01.setName("Column01");
        dbColumn01.setBoard(board01);
        dbColumn01.setId(1L);

        given(boardRepository.findById(1L)).willReturn(Optional.of(board01));
        given(userRepository.findById(1L)).willReturn(Optional.of(user01));
        given(boardRepository.existsByIdAndUserId(1L, 1L)).willReturn(true);
        given(columnRepository.save(any(Column.class))).willReturn(dbColumn01);

        // when
        Column serviceColumn01 = columnService.createColumnForUser(columnForm, 1L);

        // then
        verify(columnRepository).save(toCreateColumn01);

        assertNotNull(serviceColumn01);
        assertEquals(serviceColumn01, dbColumn01);
    }

    @Test
    public void it_should_retrieve_columns_when_it_exists_an_associated_board_id() {
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

        Column column02 = new Column();
        column02.setBoard(board01);
        column02.setName("Column02");
        column02.setId(2L);

        given(boardRepository.findById(1L)).willReturn(Optional.of(board01));
        given(userRepository.findById(1L)).willReturn(Optional.of(user01));
        given(boardRepository.existsByIdAndUserId(1L, 1L)).willReturn(true);
        given(columnRepository.findColumnsByBoard(board01)).willReturn(Arrays.asList(column01, column02));

        // when
        List<Column> dbColumns = columnService.findColumnsByBoardForUser(1L, 1L);

        // then
        assertNotNull(dbColumns);
        assertTrue(dbColumns.size() == 2);
        assertEquals(dbColumns, Arrays.asList(column01, column02));

    }

    @Test(expected = ResourceNotFoundForUserException.class)
    public void it_should_throw_an_exception_when_it_does_not_exist_an_associated_board_id() {
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

        given(boardRepository.findById(1L)).willReturn(Optional.of(board01));
        given(userRepository.findById(1L)).willReturn(Optional.of(user01));
        given(boardRepository.existsByIdAndUserId(1L, 1L)).willReturn(false);

        // when
        List<Column> dbColumns = columnService.findColumnsByBoardForUser(1L, 1L);
    }

    @Test
    public void it_should_return_column_when_retrieving_an_associated_column_id() {
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

        given(columnRepository.findById(1L)).willReturn(Optional.of(column01));
        given(boardRepository.findById(1L)).willReturn(Optional.of(board01));
        given(userRepository.findById(1L)).willReturn(Optional.of(user01));
        given(boardRepository.existsByIdAndUserId(1L, 1L)).willReturn(true);

        // when
        Column dbColumn01 = columnService.findColumnForUser(1L, 1L);

        // then
        assertNotNull(dbColumn01);
        assertEquals(dbColumn01, column01);
    }

    @Test(expected = ResourceNotFoundForUserException.class)
    public void it_should_throw_an_exception_when_retrieving_a_non_associated_column_id() {
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

        given(columnRepository.findById(1L)).willReturn(Optional.of(column01));
        given(boardRepository.findById(1L)).willReturn(Optional.of(board01));
        given(userRepository.findById(1L)).willReturn(Optional.of(user01));
        given(boardRepository.existsByIdAndUserId(1L, 1L)).willReturn(false);

        // when
        Column dbColumn01 = columnService.findColumnForUser(1L, 1L);
    }

    @Test
    public void it_should_delete_column_when_given_an_associated_column_id() {
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

        given(columnRepository.findById(1L)).willReturn(Optional.of(column01));
        given(boardRepository.findById(1L)).willReturn(Optional.of(board01));
        given(userRepository.findById(1L)).willReturn(Optional.of(user01));
        given(boardRepository.existsByIdAndUserId(1L, 1L)).willReturn(true);

        // when
        columnService.deleteColumnForUser(1L, 1L);

        // then
        verify(columnRepository).deleteById(1L);

    }

    @Test(expected = ResourceNotFoundForUserException.class)
    public void it_should_throw_an_exception_when_deleting_a_non_associated_column_id() {
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

        given(columnRepository.findById(1L)).willReturn(Optional.of(column01));
        given(boardRepository.findById(1L)).willReturn(Optional.of(board01));
        given(userRepository.findById(1L)).willReturn(Optional.of(user01));
        given(boardRepository.existsByIdAndUserId(1L, 1L)).willReturn(false);

        // when
        columnService.deleteColumnForUser(1L, 1L);
    }

    @Test
    public void it_should_update_a_column_when_given_a_valid_column_form_with_associated_column_id() {
        // given
        ColumnForm columnForm = new ColumnForm();
        columnForm.setId(1L);
        columnForm.setBoardId(1L);
        columnForm.setName("updatedColumn01");

        User user01 = new User();
        user01.setUsername("user01");
        user01.setId(1L);

        Board board01 = new Board();
        board01.setName("Board01");
        board01.setUser(user01);
        board01.setId(1L);

        Column dbColumn01 = new Column();
        dbColumn01.setId(1L);
        dbColumn01.setName("Column01");
        dbColumn01.setBoard(board01);

        Column updatedDbColumn01 = new Column();
        updatedDbColumn01.setId(1L);
        updatedDbColumn01.setName("updatedColumn01");
        updatedDbColumn01.setBoard(board01);

        given(columnRepository.findById(1L)).willReturn(Optional.of(dbColumn01));
        given(boardRepository.findById(1L)).willReturn(Optional.of(board01));
        given(userRepository.findById(1L)).willReturn(Optional.of(user01));
        given(boardRepository.existsByIdAndUserId(1L, 1L)).willReturn(true);
        given(columnRepository.save(any(Column.class))).willReturn(updatedDbColumn01);

        // when
        Column serviceUpdatedColumn01 = columnService.updateColumnForUser(columnForm, 1L);

        // then
        verify(columnRepository).save(updatedDbColumn01);

        assertNotNull(serviceUpdatedColumn01);
        assertEquals(serviceUpdatedColumn01, updatedDbColumn01);
    }

    @Test(expected = ResourceNotFoundForUserException.class)
    public void it_should_throw_an_exception_when_updating_a_card_with_a_valid_card_form_and_a_non_associated_column_id() {
        // given
        ColumnForm columnForm = new ColumnForm();
        columnForm.setId(1L);
        columnForm.setBoardId(1L);
        columnForm.setName("updatedColumn01");

        User user01 = new User();
        user01.setUsername("user01");
        user01.setId(1L);

        Board board01 = new Board();
        board01.setName("Board01");
        board01.setId(1L);

        Column dbColumn01 = new Column();
        dbColumn01.setId(1L);
        dbColumn01.setName("Column01");
        dbColumn01.setBoard(board01);

        given(columnRepository.findById(1L)).willReturn(Optional.of(dbColumn01));
        given(boardRepository.findById(1L)).willReturn(Optional.of(board01));
        given(userRepository.findById(1L)).willReturn(Optional.of(user01));
        given(boardRepository.existsByIdAndUserId(1L, 1L)).willReturn(false);

        // when
        Column serviceUpdatedColumn01 = columnService.updateColumnForUser(columnForm, 1L);
    }

}
