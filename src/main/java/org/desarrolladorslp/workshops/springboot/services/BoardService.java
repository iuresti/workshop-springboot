package org.desarrolladorslp.workshops.springboot.services;

import org.desarrolladorslp.workshops.springboot.models.Board;
import org.desarrolladorslp.workshops.springboot.models.User;

import java.util.List;

public interface BoardService {
    Board createBoard(Board board);
    List<Board> findBoardsByUser(Long userId) throws Exception;
    Board findById(Long id);
    void deleteBoard(Long id) throws Exception;
    Board updateBoard(Board board);

}
