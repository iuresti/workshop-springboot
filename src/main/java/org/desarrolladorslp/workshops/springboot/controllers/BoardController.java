package org.desarrolladorslp.workshops.springboot.controllers;

import org.desarrolladorslp.workshops.springboot.models.Board;
import org.desarrolladorslp.workshops.springboot.models.User;
import org.desarrolladorslp.workshops.springboot.services.BoardService;
import org.desarrolladorslp.workshops.springboot.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BoardController {

    private BoardService boardService;
    private UserService userService;

    public BoardController(BoardService BoardService, UserService userService){
        this.boardService = BoardService;
        this.userService = userService;
    }

    @RequestMapping(value = "/board", method = RequestMethod.POST)
    @ResponseBody
    private HttpStatus createBoard(@RequestParam(value = "userId", required = true) Long userId,@RequestParam(value = "name", required = true) String name) throws Exception {
        Board board = new Board();
        User user = userService.findById(userId);
        board.setName(name);
        board.setUser(user);
        boardService.createBoard(board);
        return HttpStatus.OK;
    }


    @RequestMapping(value = "/boards", method = RequestMethod.GET)
    @ResponseBody
    private List<Board> getBoardsByUser(@RequestParam(value="userId", required = true)Long userId) throws Exception {
        return boardService.findBoardsByUser(userId);
    }

    @RequestMapping(value = "/board/{id}", method = RequestMethod.GET)
    @ResponseBody
    private Board getBoardsById(@PathVariable("id")Long id) {
        return boardService.findById(id);
    }

    @RequestMapping(value = "/board/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    private HttpStatus deleteBoard(@PathVariable("id")Long id) throws Exception {
        boardService.deleteBoard(id);
        return HttpStatus.OK;
    }

    @RequestMapping(value = "/board/{id}", method = RequestMethod.PATCH)
    @ResponseBody
    private HttpStatus updateBoard(@PathVariable("id")Long id,@RequestParam(value = "name", required = true)String name) {
        Board board = boardService.findById(id);
        board.setName(name);
        boardService.updateBoard(board);
        return HttpStatus.OK;
    }

}
