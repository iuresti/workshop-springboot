package org.desarrolladorslp.workshops.springboot.controllers;

import java.util.List;

import org.desarrolladorslp.workshops.springboot.models.Card;
import org.desarrolladorslp.workshops.springboot.services.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/card")
public class CardController {

    private CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping
    private ResponseEntity<Card> create(@RequestBody Card card) {
        return new ResponseEntity<>(cardService.create(card), HttpStatus.CREATED);
    }


    @GetMapping("/column/{columnId}")
    private List<Card> getCardsByColumn(@PathVariable("columnId") Long columnId) {
        return cardService.findByColumn(columnId);
    }

    @GetMapping(value = "/{id}")
    private Card getCardById(@PathVariable("id") Long id) {
        return cardService.findById(id);
    }

    @DeleteMapping(value = "/{id}")
    private void deleteCard(@PathVariable("id") Long id) {
        cardService.deleteById(id);
    }

    @PutMapping
    private Card updateCard(@RequestBody Card card) {
        return cardService.update(card);
    }

    @PutMapping(value = "/move/{idColumnSource}/{idColumnTarget}")
    private void updateCard(@RequestBody Card card, @PathVariable("idColumnSource") Long idColumnSource, @PathVariable("idColumnTarget") Long idColumnTarget) {
        cardService.moveCard(card, idColumnSource, idColumnTarget);

    }
}
