package org.desarrolladorslp.workshops.springboot.repository;

import java.util.List;

import org.desarrolladorslp.workshops.springboot.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    @Query(value = "SELECT card FROM Card card WHERE card.column.id = ?1")
    List<Card> findByColumn(long idColumn);
}
