package org.desarrolladorslp.workshops.springboot.controllers;

import org.desarrolladorslp.workshops.springboot.models.Card;
import org.desarrolladorslp.workshops.springboot.services.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/card")
public class CardController {

    private CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    // User/Admin Role
    @PostMapping
    public ResponseEntity<Card> create(@RequestBody Card card) {
        return new ResponseEntity<>(cardService.create(card), HttpStatus.CREATED);
    }

    // User/Admin Role - Column must belong to current user
    @GetMapping("/column/{columnId}")
    public List<Card> getCardsByColumn(@PathVariable("columnId") Long columnId) {
        return cardService.findByColumn(columnId);
    }

    // User/Admin Role - Card must belong to current user
    @GetMapping(value = "/{id}")
    public Card getCardById(@PathVariable("id") Long id) {
        return cardService.findById(id);
    }

    // User/Admin Role - Card must belong to current user
    @DeleteMapping(value = "/{id}")
    public void deleteCard(@PathVariable("id") Long id) {
        cardService.deleteById(id);
    }

    // User/Admin Role - Card must belong to current user
    @PutMapping
    public Card updateCard(@RequestBody Card card) {
        return cardService.update(card);
    }

    // User/Admin Role - Card must belong to current user
    @PutMapping(value = "/move/{idColumnSource}/{idColumnTarget}")
    public void updateCard(@RequestBody Card card, @PathVariable("idColumnSource") Long idColumnSource, @PathVariable("idColumnTarget") Long idColumnTarget) {
        cardService.moveCard(card, idColumnSource, idColumnTarget);

    }
}
