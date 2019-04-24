package org.desarrolladorslp.workshops.springboot.controllers;

import org.desarrolladorslp.workshops.springboot.models.Column;
import org.desarrolladorslp.workshops.springboot.services.ColumnService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/column")
public class ColumnController {
    private ColumnService columnService;

    public ColumnController(ColumnService columnService) {
        this.columnService = columnService;
    }

    // User/Admin Role
    @PostMapping
    public ResponseEntity<Column> create(@RequestBody Column column) {
        return new ResponseEntity<>(columnService.create(column), HttpStatus.CREATED);
    }

    // User/Admin Role - Board must belong to current user
    @GetMapping("/board/{boardId}")
    public ResponseEntity<List<Column>> getByBoard(@PathVariable("boardId") Long boardId) {
        return new ResponseEntity<>(columnService.findByBoard(boardId), HttpStatus.OK);
    }

    // User/Admin Role - Board/Column must belong to current user
    @GetMapping(value = "/{id}")
    public ResponseEntity<Column> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(columnService.findById(id), HttpStatus.OK);
    }

    // User/Admin Role - Board/Column must belong to current user
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable("id") Long id) {
        columnService.deleteById(id);
    }

    // User/Admin Role - Board/Column must belong to current user
    @PutMapping
    public ResponseEntity<Column> update(@RequestBody Column column) {
        return new ResponseEntity<>(columnService.update(column), HttpStatus.OK);
    }


}
