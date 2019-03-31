package org.desarrolladorslp.workshops.springboot.controllers;

import java.util.List;

import org.desarrolladorslp.workshops.springboot.models.Column;
import org.desarrolladorslp.workshops.springboot.services.ColumnService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/column")
public class ColumnController {
    private ColumnService columnService;

    public ColumnController(ColumnService columnService) {
        this.columnService = columnService;
    }

    @PostMapping
    private ResponseEntity<Column> create(@RequestBody Column column) {
        return new ResponseEntity<>(columnService.create(column), HttpStatus.CREATED);
    }


    @GetMapping("/board/{boardId}")
    private ResponseEntity<List<Column>> getByBoard(@PathVariable("boardId") Long boardId) {
        return new ResponseEntity<>(columnService.findByBoard(boardId), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    private ResponseEntity<Column> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(columnService.findById(id), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    private void deleteById(@PathVariable("id") Long id) {
        columnService.deleteById(id);
    }

    @PutMapping
    private ResponseEntity<Column> update(@RequestBody Column column) {
        return new ResponseEntity<>(columnService.update(column), HttpStatus.OK);
    }


}
