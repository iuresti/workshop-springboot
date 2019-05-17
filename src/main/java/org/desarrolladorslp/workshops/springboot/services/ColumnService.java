package org.desarrolladorslp.workshops.springboot.services;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.desarrolladorslp.workshops.springboot.forms.ColumnForm;
import org.desarrolladorslp.workshops.springboot.models.Column;

public interface ColumnService {
    Column findById(Long id) throws EntityNotFoundException;

    boolean existsColumnForUser(Long columnId, Long userId);
    Column createColumnForUser(ColumnForm columnForm, Long userId);
    List<Column> findColumnsByBoardForUser(Long boardId, Long userId);
    Column findColumnForUser(Long columnId, Long userId);
    void deleteColumnForUser(Long columnId, Long userId);
    Column updateColumnForUser(ColumnForm columnForm, Long userId);
}
