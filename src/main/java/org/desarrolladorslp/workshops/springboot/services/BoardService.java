package org.desarrolladorslp.workshops.springboot.services;

import org.desarrolladorslp.workshops.springboot.models.Board;
import org.desarrolladorslp.workshops.springboot.models.User;

import java.util.List;

public interface BoardService {
    Board createBoard(Board board);
    List<Board> findBoardsByUser(Long userId);
    Board findById(Long id);
    void deleteBoard(Long id);
    Board updateBoard(Board board);

}
