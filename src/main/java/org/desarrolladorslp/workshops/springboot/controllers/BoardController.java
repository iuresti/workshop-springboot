package org.desarrolladorslp.workshops.springboot.controllers;

import java.util.List;

import org.desarrolladorslp.workshops.springboot.models.Board;
import org.desarrolladorslp.workshops.springboot.services.BoardService;
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
@RequestMapping("/api/board")
public class BoardController {

    private BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping
    private ResponseEntity<Board> create(@RequestBody Board board) {
        return new ResponseEntity<>(boardService.create(board), HttpStatus.CREATED);
    }

    @GetMapping(value = "/user/{userId}")
    private ResponseEntity<List<Board>> getByUser(@PathVariable Long userId) {
        return new ResponseEntity<>(boardService.findByUser(userId), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    private ResponseEntity<Board> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(boardService.findById(id), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    private void deleteById(@PathVariable("id") Long id) {
        boardService.deleteById(id);
    }

    @PutMapping
    private ResponseEntity<Board> updateBoard(@RequestBody Board board) {
        return new ResponseEntity<>(boardService.update(board), HttpStatus.OK);
    }

}
