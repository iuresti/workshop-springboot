package org.desarrolladorslp.workshops.springboot.services;

import org.desarrolladorslp.workshops.springboot.models.Board;
import org.desarrolladorslp.workshops.springboot.models.Column;

import java.util.List;

public interface ColumnService {
    Column createColumn(Column column);
    List<Column> findColumnsByBoard(Long boardId);
    Column findById(Long id);
    void deleteColumn(Long id);
    Column updateColumn(Column column);
}
