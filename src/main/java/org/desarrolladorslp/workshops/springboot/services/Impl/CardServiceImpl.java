package org.desarrolladorslp.workshops.springboot.services.Impl;

import org.desarrolladorslp.workshops.springboot.models.Card;
import org.desarrolladorslp.workshops.springboot.models.Column;
import org.desarrolladorslp.workshops.springboot.repository.CardRepository;
import org.desarrolladorslp.workshops.springboot.services.CardService;
import org.desarrolladorslp.workshops.springboot.services.ColumnService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    private CardRepository cardRepository;
    private ColumnService columnService;

    public CardServiceImpl(CardRepository cardRepository,ColumnService columnService){
        this.cardRepository = cardRepository;
        this.columnService = columnService;
    }

    @Override
    public Card createCard(Card card) {
        return cardRepository.save(card);
    }

    @Override
    public List<Card> findCardsByColumn(Long columnId) throws Exception {
        Column column = columnService.findById(columnId);
        return cardRepository.findByColumn(column);
    }

    @Override
    public Card findById(Long id) throws Exception {
        return cardRepository.findById(id).orElseThrow(()->new Exception("Card not found"));
    }

    @Override
    public void deleteCard(Long id) throws Exception {
        Card card = cardRepository.findById(id).orElseThrow(()->new Exception("Card not found"));
        cardRepository.delete(card);
    }

    @Override
    public Card updateCard(Card oldCard,Card newCard) throws Exception {
        if(newCard.getColumn().getBoard().getId() != oldCard.getColumn().getBoard().getId()){
            throw new Exception("Cannot perform this action, cards can be only moved in the same board");
        }
        return cardRepository.save(newCard);
    }
}
