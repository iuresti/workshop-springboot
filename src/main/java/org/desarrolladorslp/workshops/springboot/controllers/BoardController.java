package org.desarrolladorslp.workshops.springboot.controllers;

import java.security.Principal;
import java.util.List;

import org.desarrolladorslp.workshops.springboot.forms.BoardForm;
import org.desarrolladorslp.workshops.springboot.models.Board;
import org.desarrolladorslp.workshops.springboot.services.BoardService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/board")
@PreAuthorize("isAuthenticated()")
@Api(name = "Board Resource", description = "Administracion de Boards de usuario.")
@ApiAuthToken(scheme = "Bearer")
public class BoardController {

    private BoardService boardService;
    private UserService userService;

    public BoardController(BoardService boardService,
                           UserService userService) {
        this.boardService = boardService;
        this.userService = userService;
    }

    // User/Admin Role
    @RequestMapping(method = RequestMethod.POST)
    @ApiMethod(description = "Crear nueva Board de usuario.")
    public ResponseEntity<Board> create(
            @Validated(ValidationCreate.class) @RequestBody BoardForm boardForm,
            Principal principal) {
        boardForm.setUserId(currentUserId(principal.getName()));
        return new ResponseEntity<>(boardService.create(boardForm), HttpStatus.CREATED);
    }

    // User/Admin Role - Boards for the current user
    @GetMapping(value = "/forCurrentUser")
    @ApiMethod(description = "Recuperar Board para el usuario actual.")
    public ResponseEntity<List<Board>> getForCurrentUser(Principal principal) {
        return new ResponseEntity<>(
                boardService.findByUser(currentUserId(principal.getName())), HttpStatus.OK);
    }

    // Admin Role - Boards for any user
    @GetMapping(value = "/user/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiMethod(description = "Recuperar Boards para el id de usuario especificado.")
    @ApiAuthToken(roles = "ADMIN", scheme = "Bearer")
    public ResponseEntity<List<Board>> getByUser(
            @ApiPathParam(name = "userId", description = "Id de Usuario") @PathVariable Long userId) {
        return new ResponseEntity<>(boardService.findByUser(userId), HttpStatus.OK);
    }

    // User/Admin Role - Board must belong to current user
    @GetMapping(value = "/{id}")
    @ApiMethod(description = "Recuperar Board para el usuario actual.")
    public ResponseEntity<Board> getById(
            @ApiPathParam(name = "id", description = "Id de Board") @PathVariable("id") Long id,
            Principal principal) {
        return new ResponseEntity<>(
                boardService.findByIdAndUserId(id, currentUserId(principal.getName())), HttpStatus.OK);
    }

    // User/Admin Role - Board must belong to current user
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiMethod(description = "Eliminar Board de usuario.")
    public void deleteById(
            @ApiPathParam(name = "id", description = "Id de Board") @PathVariable("id") Long id,
            Principal principal) {
        boardService.findByIdAndUserId(id, currentUserId(principal.getName()));
        // If no exception was thrown by the previous line, it is safe to proceed.
        boardService.deleteById(id);
    }

    // User/Admin Role - Board must belong to current user
    @PutMapping
    @ApiMethod(description = "Actualizar Board de usuario.")
    public ResponseEntity<Board> updateBoard(
            @Validated(ValidationUpdate.class) @RequestBody BoardForm boardForm,
            Principal principal) {
        boardService.findByIdAndUserId(boardForm.getId(), currentUserId(principal.getName()));
        // If no exception was thrown by the previous line, it is safe to proceed.
        return new ResponseEntity<>(boardService.update(boardForm), HttpStatus.OK);
    }

    // User/Admin Role - Board must belong to current user
    @PostMapping(value = "/{id}")
    @ApiMethod(description = "Duplicar Board de usuario a otra Board.")
    public ResponseEntity duplicate(
            @ApiPathParam(name = "id", description = "Id de Board") @PathVariable("id") Long id,
            Principal principal) {
        boardService.findByIdAndUserId(id, currentUserId(principal.getName()));
        // If no exception was thrown by the previous line, it is safe to proceed.
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    private Long currentUserId(String username) {
        return userService.findByUsername(username).getId();
    }

}
