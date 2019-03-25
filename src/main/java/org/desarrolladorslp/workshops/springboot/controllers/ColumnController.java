package org.desarrolladorslp.workshops.springboot.controllers;

import org.desarrolladorslp.workshops.springboot.models.Board;
import org.desarrolladorslp.workshops.springboot.models.Column;
import org.desarrolladorslp.workshops.springboot.services.BoardService;
import org.desarrolladorslp.workshops.springboot.services.ColumnService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ColumnController {
    private ColumnService columnService;
    private BoardService boardService;
    public ColumnController(BoardService BoardService, ColumnService columnService){
        this.boardService = BoardService;
        this.columnService = columnService;
    }

    @RequestMapping(value = "/column", method = RequestMethod.POST)
    @ResponseBody
    private HttpStatus createColumn(@RequestParam(value = "boardId", required = true) Long boardId, @RequestParam(value = "name", required = true) String name) {
        Column column = new Column();
        Board board = boardService.findById(boardId);
        column.setName(name);
        column.setBoard(board);
        columnService.createColumn(column);
        return HttpStatus.OK;
    }



    @RequestMapping(value = "/columns", method = RequestMethod.GET)
    @ResponseBody
    private List<Column> getColumnsByBoard(@RequestParam(value="boardId", required = true)Long boardId) {
        return columnService.findColumnsByBoard(boardId);
    }

    @RequestMapping(value = "/column/{id}", method = RequestMethod.GET)
    @ResponseBody
    private Column getColumnById(@PathVariable("id")Long id) throws Exception {
        return columnService.findById(id);
    }

    @RequestMapping(value = "/column/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    private HttpStatus deleteColumn(@PathVariable("id")Long id) throws Exception {
        columnService.deleteColumn(id);
        return HttpStatus.OK;
    }

    @RequestMapping(value = "/column/{id}", method = RequestMethod.PATCH)
    @ResponseBody
    private HttpStatus updateColumn(@PathVariable("id")Long id,@RequestParam(value = "name", required = true)String name) throws Exception {
        Column column = columnService.findById(id);
        column.setName(name);
        columnService.updateColumn(column);
        return HttpStatus.OK;
    }



}
