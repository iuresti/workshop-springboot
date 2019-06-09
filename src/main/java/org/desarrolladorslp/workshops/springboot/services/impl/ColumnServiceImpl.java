package org.desarrolladorslp.workshops.springboot.services.impl;

import java.util.List;
import java.util.Objects;

import javax.persistence.EntityNotFoundException;

import org.desarrolladorslp.workshops.springboot.exceptions.ResourceNotFoundForUserException;
import org.desarrolladorslp.workshops.springboot.forms.ColumnForm;
import org.desarrolladorslp.workshops.springboot.models.Board;
import org.desarrolladorslp.workshops.springboot.models.Column;
import org.desarrolladorslp.workshops.springboot.models.User;
import org.desarrolladorslp.workshops.springboot.repository.BoardRepository;
import org.desarrolladorslp.workshops.springboot.repository.ColumnRepository;
import org.desarrolladorslp.workshops.springboot.repository.UserRepository;
import org.desarrolladorslp.workshops.springboot.services.ColumnService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ColumnServiceImpl implements ColumnService {

    private ColumnRepository columnRepository;
    private BoardRepository boardRepository;
    private UserRepository userRepository;

    public ColumnServiceImpl(ColumnRepository columnRepository,
                             BoardRepository boardRepository,
                             UserRepository userRepository) {
        this.columnRepository = columnRepository;
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Column findById(Long id) {
        Objects.requireNonNull(id, "id is required");

        return columnRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Column #%s not found", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsColumnForUser(Long columnId, Long userId) {
        Objects.requireNonNull(columnId, "columnId is required");
        Objects.requireNonNull(userId, "userId is required");

        Column column = this.findById(columnId);
        return existsBoardForUser(column.getBoard().getId(), userId);
    }

    @Override
    @Transactional
    public Column createColumnForUser(ColumnForm columnForm, Long userId) {

        Objects.requireNonNull(columnForm, "columnForm is required");
        Objects.requireNonNull(columnForm.getName(), "columnForm#name is required");
        Objects.requireNonNull(columnForm.getBoard(), "columnForm#board is required");

        Objects.requireNonNull(userId, "userId is required");

        Board board = findBoardForUser(columnForm.getBoard(), userId);
        Column column = new Column();
        column.setName(columnForm.getName());
        column.setBoard(board);
        return columnRepository.save(column);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Column> findColumnsByBoardForUser(Long boardId, Long userId) {
        Objects.requireNonNull(boardId, "board is required");
        Objects.requireNonNull(userId, "userId is required");

        Board board = findBoardForUser(boardId, userId);
        return columnRepository.findColumnsByBoard(board);
    }

    @Override
    @Transactional(readOnly = true)
    public Column findColumnForUser(Long columnId, Long userId) {
        Objects.requireNonNull(columnId, "columnId is required");
        Objects.requireNonNull(userId, "userId is required");

        if(existsColumnForUser(columnId, userId)) {
            return this.findById(columnId);
        }
        // Column not found for given userId.
        throw new ResourceNotFoundForUserException(
                String.format("Column #%d not found for User #%d", columnId, userId));
    }

    @Transactional
    private void deleteColumnById(Long id) {
        this.findById(id);
        columnRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteColumnForUser(Long columnId, Long userId) {
        Objects.requireNonNull(columnId, "columnId is required");
        Objects.requireNonNull(userId, "userId is required");

        Column column = findColumnForUser(columnId, userId);
        this.deleteColumnById(column.getId());
    }

    @Override
    @Transactional
    public Column updateColumnForUser(ColumnForm columnForm, Long userId) {
        Objects.requireNonNull(columnForm, "columnForm is required");
        Objects.requireNonNull(columnForm.getId(), "columnForm#id is required");
        Objects.requireNonNull(columnForm.getName(), "columnForm#name is required");
        Objects.requireNonNull(columnForm.getBoard(), "columnForm#board is required");

        Objects.requireNonNull(userId, "userId is required");

        Column toUpdate = findColumnForUser(columnForm.getId(), userId);
        toUpdate.setName(columnForm.getName());
        return columnRepository.save(toUpdate);
    }

    @Transactional(readOnly = true)
    private Board findBoardById(Long boardId) {
        return boardRepository.findById(boardId).
                orElseThrow(() -> new EntityNotFoundException(String.format("Board #%s not found", boardId)));
    }

    @Transactional(readOnly = true)
    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User #%s not found", userId)));
    }

    @Transactional(readOnly = true)
    private boolean existsBoardForUser(Long boardId, Long userId) {
        findBoardById(boardId);
        findUserById(userId);
        return boardRepository.existsByIdAndUserId(boardId, userId);
    }

    @Transactional(readOnly = true)
    private Board findBoardForUser(Long boardId, Long userId) {
        if(existsBoardForUser(boardId, userId)) {
            return findBoardById(boardId);
        }
        throw new ResourceNotFoundForUserException(
                String.format("Board #%s not found for User #%s", boardId, userId));
    }
}
