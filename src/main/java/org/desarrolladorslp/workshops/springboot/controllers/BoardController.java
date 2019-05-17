package org.desarrolladorslp.workshops.springboot.controllers;

import org.desarrolladorslp.workshops.springboot.forms.BoardForm;
import org.desarrolladorslp.workshops.springboot.models.Board;
import org.desarrolladorslp.workshops.springboot.services.BoardService;
import org.desarrolladorslp.workshops.springboot.services.UserService;
import org.desarrolladorslp.workshops.springboot.validation.ValidationCreate;
import org.desarrolladorslp.workshops.springboot.validation.ValidationUpdate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/board")
@PreAuthorize("isAuthenticated()")
public class BoardController {

    private BoardService boardService;
    private UserService userService;

    public BoardController(BoardService boardService,
                           UserService userService) {
        this.boardService = boardService;
        this.userService = userService;
    }

    // User/Admin Role
    @PostMapping
    public ResponseEntity<Board> create(
            @Validated(ValidationCreate.class) @RequestBody BoardForm boardForm,
            Principal principal) {
        boardForm.setUserId(currentUserId(principal.getName()));
        return new ResponseEntity<>(boardService.create(boardForm), HttpStatus.CREATED);
    }

    // User/Admin Role - Boards for the current user
    @GetMapping(value = "/forCurrentUser")
    public ResponseEntity<List<Board>> getForCurrentUser(Principal principal) {
        return new ResponseEntity<>(
                boardService.findByUser(currentUserId(principal.getName())), HttpStatus.OK);
    }

    // Admin Role - Boards for any user
    @GetMapping(value = "/user/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Board>> getByUser(@PathVariable Long userId) {
        return new ResponseEntity<>(boardService.findByUser(userId), HttpStatus.OK);
    }

    // User/Admin Role - Board must belong to current user
    @GetMapping(value = "/{id}")
    public ResponseEntity<Board> getById(@PathVariable("id") Long id, Principal principal) {
        return new ResponseEntity<>(
                boardService.findByIdAndUserId(id, currentUserId(principal.getName())), HttpStatus.OK);
    }

    // User/Admin Role - Board must belong to current user
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable("id") Long id, Principal principal) {
        boardService.findByIdAndUserId(id,currentUserId(principal.getName()));
        // If no exception was thrown by the previous line, it is safe to proceed.
        boardService.deleteById(id);
    }

    // User/Admin Role - Board must belong to current user
    @PutMapping
    public ResponseEntity<Board> updateBoard(
            @Validated(ValidationUpdate.class) @RequestBody BoardForm boardForm,
            Principal principal) {
        boardService.findByIdAndUserId(boardForm.getId(),currentUserId(principal.getName()));
        // If no exception was thrown by the previous line, it is safe to proceed.
        return new ResponseEntity<>(boardService.update(boardForm), HttpStatus.OK);
    }

    // User/Admin Role - Board must belong to current user
    @PostMapping(value = "/{id}")
    public ResponseEntity duplicate(@PathVariable("id") Long id, Principal principal) {
        boardService.findByIdAndUserId(id, currentUserId(principal.getName()));
        // If no exception was thrown by the previous line, it is safe to proceed.
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    private Long currentUserId(String username) {
        return userService.findByUsername(username).getId();
    }

}
