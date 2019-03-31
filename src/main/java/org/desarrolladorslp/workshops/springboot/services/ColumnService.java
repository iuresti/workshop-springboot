package org.desarrolladorslp.workshops.springboot.services;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.desarrolladorslp.workshops.springboot.models.Column;

public interface ColumnService {
    Column create(Column column) throws EntityNotFoundException, IllegalArgumentException;

    List<Column> findByBoard(Long boardId);

    Column findById(Long id) throws EntityNotFoundException;

    void deleteById(Long id) throws EntityNotFoundException;

    Column update(Column column);
}
