package org.desarrolladorslp.workshops.springboot.controllers;

import org.desarrolladorslp.workshops.springboot.forms.ColumnForm;
import org.desarrolladorslp.workshops.springboot.models.Column;
import org.desarrolladorslp.workshops.springboot.services.ColumnService;
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
@RequestMapping("/api/column")
@PreAuthorize("isAuthenticated()")
@Api(name="Column Resource", description = "Administracion de Columns de usuario.")
@ApiAuthToken(scheme = "Bearer")
public class ColumnController {

    private ColumnService columnService;
    private UserService userService;

    public ColumnController(ColumnService columnService, UserService userService) {
        this.columnService = columnService;
        this.userService = userService;
    }

    // User/Admin Role
    // createColumnForUser(columnForm, userId) -> column
    @RequestMapping(method = RequestMethod.POST)
    @ApiMethod(description = "Crear nueva Column de usuario.")
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
    @ApiMethod(description = "Recuperar Columns para la Board especificada.")
    public ResponseEntity<List<Column>> getByBoard(
            @ApiPathParam(name = "boardId", description = "Id de Board") @PathVariable("boardId") Long boardId,
            Principal principal) {
        return new ResponseEntity<>(
                columnService.findColumnsByBoardForUser(
                        boardId, userService.findByUsername(principal.getName()).getId()),
                HttpStatus.OK);
    }

    // User/Admin Role - Board/Column must belong to current user
    @GetMapping(value = "/{id}")
    @ApiMethod(description = "Recuperar Column de usuario.")
    // findColumnForUser(columnId, userId) : Column
    public ResponseEntity<Column> getById(
            @ApiPathParam(name = "id", description = "Id de Column") @PathVariable("id") Long id,
            Principal principal) {
        return new ResponseEntity<>(
                columnService.findColumnForUser(
                        id, userService.findByUsername(principal.getName()).getId()),
                HttpStatus.OK);
    }

    // User/Admin Role - Board/Column must belong to current user
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiMethod(description = "Eliminar Column de usuario.")
    // deleteColumnForUser(columnId, userId) -> void
    public void deleteById(
            @ApiPathParam(name = "id", description = "Id de Column") @PathVariable("id") Long id,
            Principal principal) {
        columnService.deleteColumnForUser(id, userService.findByUsername(principal.getName()).getId());
    }

    // User/Admin Role - Board/Column must belong to current user
    // updateColumnForUser(columnForm, userId) -> column
    @PutMapping
    @ApiMethod(description = "Actualizar Column de usuario.")
    public ResponseEntity<Column> update(
            @Validated(ValidationUpdate.class) @RequestBody ColumnForm columnForm,
            Principal principal) {
        return new ResponseEntity<>(
                columnService.updateColumnForUser(
                        columnForm, userService.findByUsername(principal.getName()).getId()),
                HttpStatus.OK);
    }

}
