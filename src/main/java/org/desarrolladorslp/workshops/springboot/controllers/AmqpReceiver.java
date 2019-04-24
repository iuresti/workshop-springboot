package org.desarrolladorslp.workshops.springboot.controllers;

import org.desarrolladorslp.workshops.springboot.services.BoardService;
import org.springframework.stereotype.Component;

//@Component
public class AmqpReceiver {

    private BoardService boardService;

    public AmqpReceiver(BoardService boardService) {
        this.boardService = boardService;
    }

    public void receiveMessage(Long id) {
        boardService.duplicate(id);
    }

}
