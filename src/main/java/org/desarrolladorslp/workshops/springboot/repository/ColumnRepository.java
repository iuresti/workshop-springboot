package org.desarrolladorslp.workshops.springboot.repository;

import java.util.List;

import org.desarrolladorslp.workshops.springboot.models.Board;
import org.desarrolladorslp.workshops.springboot.models.Column;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ColumnRepository extends JpaRepository<Column, Long> {
    List<Column> findColumnsByBoard(Board board);

    @Query(value = "SELECT count(column)>0 FROM Column column " +
            "WHERE column.id = ?1 AND column.board.user.id = ?2")
    boolean existsByIdAndUserId(Long columnId, Long userId);
}
