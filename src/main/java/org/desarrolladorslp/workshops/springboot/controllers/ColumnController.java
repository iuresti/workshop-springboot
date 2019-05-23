package org.desarrolladorslp.workshops.springboot.controllers;

import org.desarrolladorslp.workshops.springboot.forms.ColumnForm;
import org.desarrolladorslp.workshops.springboot.models.Column;
import org.desarrolladorslp.workshops.springboot.services.ColumnService;
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
@RequestMapping("/api/column")
@PreAuthorize("isAuthenticated()")
public class ColumnController {

    private ColumnService columnService;
    private UserService userService;

    public ColumnController(ColumnService columnService, UserService userService) {
        this.columnService = columnService;
        this.userService = userService;
    }

    // User/Admin Role
    // createColumnForUser(columnForm, userId) -> column
    @PostMapping
    public ResponseEntity<Column> create(
            @Validated(ValidationCreate.class) @RequestBody ColumnForm columnForm,
            Principal principal) {
        long userId = userService.findByUsername(principal.getName()).getId();
        return new ResponseEntity<>(columnService.createColumnForUser(columnForm, userId),
                HttpStatus.CREATED);
    }

    // User/Admin Role - Board must belong to current user
    // findColumnsByBoardForUser(boardId, userId) -> List<Column>
    @GetMapping("/board/{boardId}")
    public ResponseEntity<List<Column>> getByBoard(@PathVariable("boardId") Long boardId,
                                                   Principal principal) {
        return new ResponseEntity<>(
                columnService.findColumnsByBoardForUser(
                        boardId, userService.findByUsername(principal.getName()).getId()),
                HttpStatus.OK);
    }

    // User/Admin Role - Board/Column must belong to current user
    @GetMapping(value = "/{id}")
    // findColumnForUser(columnId, userId) : Column
    public ResponseEntity<Column> getById(@PathVariable("id") Long id,
                                          Principal principal) {
        return new ResponseEntity<>(
                columnService.findColumnForUser(
                        id, userService.findByUsername(principal.getName()).getId()),
                HttpStatus.OK);
    }

    // User/Admin Role - Board/Column must belong to current user
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    // deleteColumnForUser(columnId, userId) -> void
    public void deleteById(@PathVariable("id") Long id, Principal principal) {
        columnService.deleteColumnForUser(id, userService.findByUsername(principal.getName()).getId());
    }

    // User/Admin Role - Board/Column must belong to current user
    // updateColumnForUser(columnForm, userId) -> column
    @PutMapping
    public ResponseEntity<Column> update(
            @Validated(ValidationUpdate.class) @RequestBody ColumnForm columnForm,
            Principal principal) {
        return new ResponseEntity<>(
                columnService.updateColumnForUser(
                        columnForm, userService.findByUsername(principal.getName()).getId()),
                HttpStatus.OK);
    }

}
