package org.desarrolladorslp.workshops.springboot.services.Impl;

import java.util.List;
import java.util.Objects;

import javax.persistence.EntityNotFoundException;

import org.desarrolladorslp.workshops.springboot.models.Card;
import org.desarrolladorslp.workshops.springboot.models.Column;
import org.desarrolladorslp.workshops.springboot.repository.CardRepository;
import org.desarrolladorslp.workshops.springboot.repository.ColumnRepository;
import org.desarrolladorslp.workshops.springboot.services.CardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CardServiceImpl implements CardService {

    private CardRepository cardRepository;
    private ColumnRepository columnRepository;

    public CardServiceImpl(CardRepository cardRepository, ColumnRepository columnRepository) {
        this.cardRepository = cardRepository;
        this.columnRepository = columnRepository;
    }

    @Override
    @Transactional
    public Card create(Card card) {
        if (Objects.isNull(card.getColumn())) {
            throw new IllegalArgumentException("Column required");
        }
        Column column = columnRepository.findById(card.getColumn().getId()).orElseThrow(() -> new EntityNotFoundException("Column not found"));

        card.setColumn(column);

        return cardRepository.save(card);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Card> findByColumn(Long columnId) {
        return cardRepository.findByColumn(columnId);
    }

    @Override
    @Transactional(readOnly = true)
    public Card findById(Long id) {
        return cardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Card not found"));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Card not found"));
        cardRepository.delete(card);
    }

    @Override
    @Transactional
    public Card update(Card card) {
        return cardRepository.save(card);
    }

    @Override
    @Transactional
    public void moveCard(Card card, Long idColumnSource, Long idColumnTarget) {
        Card storedCard = cardRepository.findById(card.getId()).orElseThrow(() -> new EntityNotFoundException("Card not found"));

        if (!idColumnSource.equals(storedCard.getColumn().getId())) {
            throw new IllegalArgumentException("Card does not belong to idColumnSource specified");
        }

        Column column = columnRepository.findById(idColumnTarget).orElseThrow(() -> new EntityNotFoundException("target column does not exist"));

        storedCard.setColumn(column);

        cardRepository.save(storedCard);
    }
}
