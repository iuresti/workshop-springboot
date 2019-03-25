package org.desarrolladorslp.workshops.springboot.controllers;

import org.desarrolladorslp.workshops.springboot.models.Card;
import org.desarrolladorslp.workshops.springboot.models.Column;
import org.desarrolladorslp.workshops.springboot.services.CardService;
import org.desarrolladorslp.workshops.springboot.services.ColumnService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CardController {

    private CardService cardService;
    private ColumnService columnService;

    public CardController(CardService cardService,ColumnService columnService){
        this.cardService = cardService;
        this.columnService = columnService;
    }

    @RequestMapping(value = "/card", method = RequestMethod.POST)
    @ResponseBody
    private HttpStatus createCard(@RequestParam(value = "columnId", required = true) Long columnId, @RequestParam(value = "description", required = true) String description) throws Exception {
        Card card = new Card();
        Column column = columnService.findById(columnId);
        card.setDescription(description);
        card.setColumn(column);
        cardService.createCard(card);
        return HttpStatus.OK;
    }



    @RequestMapping(value = "/cards", method = RequestMethod.GET)
    @ResponseBody
    private List<Card> getCardsByColumn(@RequestParam(value="columnId", required = true)Long columnId) throws Exception {
        return cardService.findCardsByColumn(columnId);
    }

    @RequestMapping(value = "/card/{id}", method = RequestMethod.GET)
    @ResponseBody
    private Card getCardById(@PathVariable("id")Long id) throws Exception {
        return cardService.findById(id);
    }

    @RequestMapping(value = "/card/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    private HttpStatus deleteCard(@PathVariable("id")Long id) throws Exception {
        cardService.deleteCard(id);
        return HttpStatus.OK;
    }

    @RequestMapping(value = "/card/{id}", method = RequestMethod.PATCH)
    @ResponseBody
    private HttpStatus updateCard(@PathVariable("id")Long id,@RequestParam(value = "description", required = true)String description,
                                  @RequestParam(value = "columnId", required = true)Long columnId) throws Exception {
        Card newCard = cardService.findById(id);
        Card oldCard = new Card();
        oldCard.setColumn(newCard.getColumn());
        Column column = columnService.findById(columnId);
        newCard.setColumn(column);
        newCard.setDescription(description);
        cardService.updateCard(oldCard,newCard);
        return HttpStatus.OK;
    }

}
