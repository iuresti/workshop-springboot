package org.desarrolladorslp.workshops.springboot.services;

import org.desarrolladorslp.workshops.springboot.models.Card;

import java.util.List;

public interface CardService {
    Card createCard(Card card);
    List<Card> findCardsByColumn(Long columnId) throws Exception;
    Card findById(Long id) throws Exception;
    void deleteCard(Long id) throws Exception;
    Card updateCard(Card oldCard,Card newCard) throws Exception;
}
