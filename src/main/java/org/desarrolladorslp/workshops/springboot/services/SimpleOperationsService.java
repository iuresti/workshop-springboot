package org.desarrolladorslp.workshops.springboot.services;

import java.util.List;

import org.desarrolladorslp.workshops.springboot.forms.CardForm;
import org.desarrolladorslp.workshops.springboot.forms.ColumnForm;
import org.desarrolladorslp.workshops.springboot.models.Board;
import org.desarrolladorslp.workshops.springboot.models.Card;
import org.desarrolladorslp.workshops.springboot.models.Column;
import org.desarrolladorslp.workshops.springboot.models.User;

public interface SimpleOperationsService {

    // User operations
    long findUserId(String username);

    User findUserById(Long id);

    User findUserByUsername(String username);

    // Board operations
    boolean existsBoardForUser(Long boardId, Long userId);

    Board findBoardForUser(Long boardId, Long userId);

    // Column operations
    boolean existsColumnForUser(Long columnId, Long userId);

    Column createColumnForUser(ColumnForm columnForm, Long userId);

    List<Column> findColumnsByBoardForUser(Long boardId, Long userId);

    Column findColumnForUser(Long columnId, Long userId);

    void deleteColumnForUser(Long columnId, Long userId);

    Column updateColumnForUser(ColumnForm columnForm, Long userId);

    // Card Operations
    boolean existsCardForUser(Long cardId, Long userId);

    Card createCardForUser(CardForm cardForm, Long userId);

    List<Card> findCardsByColumnForUser(Long columnId, Long userId);

    Card findCardForUser(Long cardId, Long userId);

    void deleteCardForUser(Long cardId, Long userId);

    Card updateCardForUser(CardForm cardForm, Long userId);

    void moveCardForUser(Long idCard, Long idColumnTarget, Long userId);

}
