package org.desarrolladorslp.workshops.springboot.repository;

import org.desarrolladorslp.workshops.springboot.models.Board;
import org.desarrolladorslp.workshops.springboot.models.Column;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColumnRepository extends JpaRepository<Column,Long> {
    List<Column> findColumnsByBoard(Board board);
}
