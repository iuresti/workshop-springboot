package org.desarrolladorslp.workshops.springboot.services;

import java.util.List;

import org.desarrolladorslp.workshops.springboot.models.Card;

public interface CardService {
    Card create(Card card);

    List<Card> findByColumn(Long columnId);

    Card findById(Long id);

    void deleteById(Long id);

    Card update(Card card);

    void moveCard(Card card, Long idColumnSource, Long idColumnTarget);
}
