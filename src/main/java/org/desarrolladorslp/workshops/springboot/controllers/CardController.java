package org.desarrolladorslp.workshops.springboot.controllers;

import org.desarrolladorslp.workshops.springboot.forms.CardForm;
import org.desarrolladorslp.workshops.springboot.models.Card;
import org.desarrolladorslp.workshops.springboot.services.CardService;
import org.desarrolladorslp.workshops.springboot.services.UserService;
import org.desarrolladorslp.workshops.springboot.validation.ValidationCreate;
import org.desarrolladorslp.workshops.springboot.validation.ValidationUpdate;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiAuthToken;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiPathParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/card")
@PreAuthorize("isAuthenticated()")
@Api(name="Card Resource", description = "Administracion de Cards de usuario.")
@ApiAuthToken(scheme = "Bearer")
public class CardController {

    private CardService cardService;
    private UserService userService;

    public CardController(CardService cardService,
                          UserService userService) {
        this.cardService = cardService;
        this.userService = userService;
    }

    // User/Admin Role
    // createCardForUser(CardForm, userId) -> Card
    @RequestMapping(method = RequestMethod.POST)
    @ApiMethod(description = "Crear nueva Card de usuario.")
    public ResponseEntity<Card> create(
            @Validated(ValidationCreate.class) @RequestBody CardForm cardForm,
            Principal principal) {
        return new ResponseEntity<>(cardService.createCardForUser(
                cardForm, userService.findByUsername(principal.getName()).getId()),
                HttpStatus.CREATED);
    }

    // User/Admin Role - Column must belong to current user
    // findCardsByColumnForUser(columnId, userId) -> List<Column>
    @GetMapping("/column/{columnId}")
    @ApiMethod(description = "Recuperar Cards para la Column especificada.")
    public List<Card> getCardsByColumn(
            @ApiPathParam(name = "columnId", description = "Id de Column") @PathVariable("columnId") Long columnId,
            Principal principal) {
        return cardService.findCardsByColumnForUser(
                columnId, userService.findByUsername(principal.getName()).getId());
    }

    // User/Admin Role - Card must belong to current user
    // findCardForUser(cardId, userId) -> Column
    @GetMapping(value = "/{id}")
    @ApiMethod(description = "Recuperar Card de usuario.")
    public Card getCardById(@ApiPathParam(name = "id", description = "Id de Card") @PathVariable("id") Long id,
                            Principal principal) {
        return cardService.findCardForUser(id,
                userService.findByUsername(principal.getName()).getId());
    }

    // User/Admin Role - Card must belong to current user
    // deleteCardForUser(cardId, userId) -> void
    @DeleteMapping(value = "/{id}")
    @ApiMethod(description = "Eliminar Card de usuario.")
    public void deleteCard(@ApiPathParam(name = "id", description = "Id de Card") @PathVariable("id") Long id,
                           Principal principal) {
        cardService.deleteCardForUser(id,
                userService.findByUsername(principal.getName()).getId());
    }

    // User/Admin Role - Card must belong to current user
    // updateCardForUser(cardForm, userId) -> Card
    @PutMapping
    @ApiMethod(description = "Actualizar Card de usuario.")
    public Card updateCard(@Validated(ValidationUpdate.class) @RequestBody CardForm cardForm,
                           Principal principal) {
        return cardService.updateCardForUser(cardForm,
                userService.findByUsername(principal.getName()).getId());
    }

    // User/Admin Role - Card must belong to current user
    // moveCardForUser(cardId, idColumnSource, idColumnTarget) -> void
    @PutMapping(value = "/move/{idCard}/{idColumnTarget}")
    @ApiMethod(description = "Mover Card de usuario a Column de usuario.")
    public void moveCard(
            @ApiPathParam(name = "idCard", description = "Id de Card") @PathVariable("idCard") Long idCard,
            @ApiPathParam(name = "idColumnTarget", description = "Id de Target Column") @PathVariable("idColumnTarget") Long idColumnTarget,
            Principal principal) {
        cardService.moveCardForUser(idCard, idColumnTarget,
                userService.findByUsername(principal.getName()).getId());

    }
}
