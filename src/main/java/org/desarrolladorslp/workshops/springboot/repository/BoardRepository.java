package org.desarrolladorslp.workshops.springboot.repository;

import org.desarrolladorslp.workshops.springboot.models.Board;
import org.desarrolladorslp.workshops.springboot.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findBoardsByUser(User user);
    Board findBoardById(Long id);
}
