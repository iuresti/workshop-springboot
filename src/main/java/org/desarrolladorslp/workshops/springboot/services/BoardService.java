package org.desarrolladorslp.workshops.springboot.services;

import org.desarrolladorslp.workshops.springboot.forms.BoardForm;
import org.desarrolladorslp.workshops.springboot.models.Board;

import java.util.List;

public interface BoardService {
    Board create(BoardForm board);

    List<Board> findByUser(Long userId);

    Board findById(Long id);

    Board findByIdAndUserId(Long id, Long userId);

    void deleteById(Long id);

    Board update(BoardForm boardForm);

    Board duplicate(Long id);
}
