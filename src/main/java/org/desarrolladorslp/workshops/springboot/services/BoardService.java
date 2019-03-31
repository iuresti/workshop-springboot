package org.desarrolladorslp.workshops.springboot.services;

import java.util.List;

import org.desarrolladorslp.workshops.springboot.models.Board;

public interface BoardService {
    Board create(Board board);

    List<Board> findByUser(Long userId);

    Board findById(Long id);

    void deleteById(Long id);

    Board update(Board board);
}
