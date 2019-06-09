package org.desarrolladorslp.workshops.springboot.repository;

import java.util.List;
import java.util.Optional;

import org.desarrolladorslp.workshops.springboot.models.Board;
import org.desarrolladorslp.workshops.springboot.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findBoardsByUser(User user);

    Optional<Board> findByIdAndUserId(Long id, Long userId);

    boolean existsByIdAndUserId(Long boardId, Long userId);
}
