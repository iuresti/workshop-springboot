package org.desarrolladorslp.workshops.springboot.services.impl;

import java.util.List;
import java.util.Objects;

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
import org.desarrolladorslp.workshops.springboot.services.CardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CardServiceImpl implements CardService {

    private CardRepository cardRepository;
    private ColumnRepository columnRepository;
    private UserRepository userRepository;

    public CardServiceImpl(CardRepository cardRepository,
                           ColumnRepository columnRepository,
                           UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.columnRepository = columnRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Card findById(Long id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Card #%s not found", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsCardForUser(Long cardId, Long userId) {
        Card card = this.findById(cardId);
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
            return this.findById(cardId);
        }
        // Card not found for given userId.
        throw new ResourceNotFoundForUserException(
                String.format("Card #%d not found for User #%d", cardId, userId));
    }

    @Transactional
    private void deleteCardById(Long id) {
        this.findById(id);
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

    @Transactional(readOnly = true)
    private Column findColumnById(Long columnId) {
        return columnRepository.findById(columnId).
                orElseThrow(() -> new EntityNotFoundException(String.format("Column #%s not found", columnId)));
    }

    @Transactional(readOnly = true)
    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User #%s not found", userId)));
    }

    @Transactional(readOnly = true)
    private boolean existsColumnForUser(Long columnId, Long userId) {
        findColumnById(columnId);
        findUserById(userId);
        return columnRepository.existsByIdAndUserId(columnId, userId);
    }

    @Transactional(readOnly = true)
    private Column findColumnForUser(Long columnId, Long userId) {
        if(existsColumnForUser(columnId, userId)) {
            return findColumnById(columnId);
        }
        throw new ResourceNotFoundForUserException(
                String.format("Column #%s not found for User #%s", columnId, userId));
    }

}
