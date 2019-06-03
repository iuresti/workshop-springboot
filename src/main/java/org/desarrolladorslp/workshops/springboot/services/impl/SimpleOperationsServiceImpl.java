package org.desarrolladorslp.workshops.springboot.services.impl;

import lombok.AllArgsConstructor;
import org.desarrolladorslp.workshops.springboot.exceptions.ResourceNotFoundForUserException;
import org.desarrolladorslp.workshops.springboot.forms.CardForm;
import org.desarrolladorslp.workshops.springboot.forms.ColumnForm;
import org.desarrolladorslp.workshops.springboot.models.Board;
import org.desarrolladorslp.workshops.springboot.models.Card;
import org.desarrolladorslp.workshops.springboot.models.Column;
import org.desarrolladorslp.workshops.springboot.models.User;
import org.desarrolladorslp.workshops.springboot.repository.BoardRepository;
import org.desarrolladorslp.workshops.springboot.repository.CardRepository;
import org.desarrolladorslp.workshops.springboot.repository.ColumnRepository;
import org.desarrolladorslp.workshops.springboot.repository.UserRepository;
import org.desarrolladorslp.workshops.springboot.services.SimpleOperationsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@AllArgsConstructor
public class SimpleOperationsServiceImpl implements SimpleOperationsService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final ColumnRepository columnRepository;
    private final CardRepository cardRepository;

    // User operations
    @Override
    @Transactional(readOnly = true)
    public long findUserId(String username) {
        return this.findUserByUsername(username).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User #%s not found", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Username '%s' not found", username)));
    }

    // Board operations
    @Transactional(readOnly = true)
    private Board findBoardById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Board #%s not found", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsBoardForUser(Long boardId, Long userId) {
        this.findUserById(userId);
        Board board = this.findBoardById(boardId);
        return board.getUser().getId() == userId;
    }

    @Override
    @Transactional(readOnly = true)
    public Board findBoardForUser(Long boardId, Long userId) {
        if(existsBoardForUser(boardId, userId)) {
            return this.findBoardById(boardId);
        }
        // Column not found for given userId
        throw new ResourceNotFoundForUserException(
                String.format("Board #%d not found for User #%d", boardId, userId));
    }

    // Column operations
    @Transactional(readOnly = true)
    private Column findColumnById(Long id) {
        return columnRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Column #%s not found", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsColumnForUser(Long columnId, Long userId) {
        Column column = findColumnById(columnId);
        return existsBoardForUser(column.getBoard().getId(), userId);
    }

    @Override
    @Transactional
    public Column createColumnForUser(ColumnForm columnForm, Long userId) {
        Board board = findBoardForUser(columnForm.getBoardId(), userId);
        Column column = new Column();
        column.setName(columnForm.getName());
        column.setBoard(board);
        return columnRepository.save(column);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Column> findColumnsByBoardForUser(Long boardId, Long userId) {
        Board board = findBoardForUser(boardId, userId);
        return columnRepository.findColumnsByBoard(board);
    }

    @Override
    @Transactional(readOnly = true)
    public Column findColumnForUser(Long columnId, Long userId) {
        if(existsColumnForUser(columnId, userId)) {
            return this.findColumnById(columnId);
        }
        // Column not found for given userId.
        throw new ResourceNotFoundForUserException(
                String.format("Column #%d not found for User #%d", columnId, userId));
    }

    @Transactional
    private void deleteColumnById(Long id) {
        this.findColumnById(id);
        columnRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteColumnForUser(Long columnId, Long userId) {
        Column column = findColumnForUser(columnId, userId);
        this.deleteColumnById(column.getId());
    }

    @Override
    @Transactional
    public Column updateColumnForUser(ColumnForm columnForm, Long userId) {
        Column toUpdate = findColumnForUser(columnForm.getId(), userId);
        toUpdate.setName(columnForm.getName());
        return columnRepository.save(toUpdate);
    }

    // Card operations
    @Transactional(readOnly = true)
    private Card findCardById(Long id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Card #%s not found", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsCardForUser(Long cardId, Long userId) {
        Card card = findCardById(cardId);
        return existsColumnForUser(card.getColumn().getId(), userId);
    }

    @Override
    @Transactional
    public Card createCardForUser(CardForm cardForm, Long userId) {
        Column column = findColumnForUser(cardForm.getColumnId(), userId);
        Card card = new Card();
        card.setDescription(cardForm.getDescription());
        card.setColumn(column);
        return cardRepository.save(card);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Card> findCardsByColumnForUser(Long columnId, Long userId) {
        Column column = findColumnForUser(columnId, userId);
        return cardRepository.findByColumn(column.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Card findCardForUser(Long cardId, Long userId) {
        if(existsCardForUser(cardId, userId)) {
            return findCardById(cardId);
        }
        // Card not found for given userId.
        throw new ResourceNotFoundForUserException(
                String.format("Card #%d not found for User #%d", cardId, userId));
    }

    @Transactional
    private void deleteCardById(Long id) {
        this.findCardById(id);
        cardRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteCardForUser(Long cardId, Long userId) {
        Card card = findCardForUser(cardId, userId);
        deleteCardById(card.getId());
    }

    @Override
    @Transactional
    public Card updateCardForUser(CardForm cardForm, Long userId) {
        Card toUpdate = findCardForUser(cardForm.getId(), userId);
        toUpdate.setDescription(cardForm.getDescription());
        return cardRepository.save(toUpdate);
    }

    @Override
    @Transactional
    public void moveCardForUser(Long idCard, Long idColumnTarget, Long userId) {
        Card storedCard = findCardForUser(idCard, userId);
        Column targetColumn = findColumnForUser(idColumnTarget, userId);
        storedCard.setColumn(targetColumn);
        cardRepository.save(storedCard);
    }

}
