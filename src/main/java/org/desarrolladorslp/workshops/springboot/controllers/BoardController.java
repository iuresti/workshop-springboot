package org.desarrolladorslp.workshops.springboot.controllers;

import java.util.List;

import org.desarrolladorslp.workshops.springboot.models.Board;
import org.desarrolladorslp.workshops.springboot.services.BoardService;
import org.desarrolladorslp.workshops.springboot.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BoardController {

    private BoardService boardService;
    private UserService userService;

    public BoardController(BoardService BoardService, UserService userService) {
        this.boardService = BoardService;
        this.userService = userService;
    }

    @PostMapping(value = "/board")
    private ResponseEntity<Board> createBoard(@RequestBody Board board) throws Exception {

        return new ResponseEntity<>(boardService.createBoard(board), HttpStatus.CREATED);
    }

    @GetMapping(value = "/boards/{userId}")
    private ResponseEntity<List<Board>> getBoardsByUser(@PathVariable Long userId) throws Exception {
        return new ResponseEntity<>(boardService.findBoardsByUser(userId), HttpStatus.OK);
    }

    @RequestMapping(value = "/board/{id}", method = RequestMethod.GET)
    @ResponseBody
    private Board getBoardsById(@PathVariable("id") Long id) {
        return boardService.findById(id);
    }

    @RequestMapping(value = "/board/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    private HttpStatus deleteBoard(@PathVariable("id") Long id) throws Exception {
        boardService.deleteBoard(id);
        return HttpStatus.OK;
    }

    @RequestMapping(value = "/board/{id}", method = RequestMethod.PATCH)
    @ResponseBody
    private HttpStatus updateBoard(@PathVariable("id") Long id, @RequestParam(value = "name", required = true) String name) {
        Board board = boardService.findById(id);
        board.setName(name);
        boardService.updateBoard(board);
        return HttpStatus.OK;
    }

}
