package org.desarrolladorslp.workshops.springboot.services;

import java.util.List;

import org.desarrolladorslp.workshops.springboot.forms.CardForm;
import org.desarrolladorslp.workshops.springboot.models.Card;

public interface CardService {
    Card findById(Long id);

    boolean existsCardForUser(Long cardId, Long userId);

    Card createCardForUser(CardForm cardForm, Long userId);

    List<Card> findCardsByColumnForUser(Long columnId, Long userId);

    Card findCardForUser(Long cardId, Long userId);

    void deleteCardForUser(Long cardId, Long userId);

    Card updateCardForUser(CardForm cardForm, Long userId);

    void moveCardForUser(Long idCard, Long idColumnTarget, Long userId);
}
