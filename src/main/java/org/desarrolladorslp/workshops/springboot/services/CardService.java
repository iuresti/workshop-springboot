package org.desarrolladorslp.workshops.springboot.services;

import org.desarrolladorslp.workshops.springboot.models.Card;

import java.util.List;

public interface CardService {
    Card createCard(Card card);
    List<Card> findCardsByColumn(Long columnId);
    Card findById(Long id);
    void deleteCard(Long id);
    Card updateCard(Card oldCard,Card newCard) throws Exception;
}
