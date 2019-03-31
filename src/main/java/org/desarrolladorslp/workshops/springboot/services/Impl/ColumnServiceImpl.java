package org.desarrolladorslp.workshops.springboot.services.Impl;

import java.util.List;
import java.util.Objects;

import javax.persistence.EntityNotFoundException;

import org.desarrolladorslp.workshops.springboot.models.Board;
import org.desarrolladorslp.workshops.springboot.models.Column;
import org.desarrolladorslp.workshops.springboot.repository.BoardRepository;
import org.desarrolladorslp.workshops.springboot.repository.ColumnRepository;
import org.desarrolladorslp.workshops.springboot.services.ColumnService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ColumnServiceImpl implements ColumnService {

    private ColumnRepository columnRepository;
    private BoardRepository boardRepository;

    public ColumnServiceImpl(ColumnRepository columnRepository, BoardRepository boardRepository) {
        this.columnRepository = columnRepository;
        this.boardRepository = boardRepository;
    }

    @Override
    @Transactional
    public Column create(Column column) {
        if (Objects.isNull(column.getBoard())) {
            throw new IllegalArgumentException("User required");
        }
        Board board = boardRepository.findById(column.getBoard().getId()).orElseThrow(() -> new EntityNotFoundException("Board not found"));

        column.setBoard(board);

        return columnRepository.save(column);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Column> findByBoard(Long boardId) {

        if (Objects.isNull(boardId)) {
            throw new IllegalArgumentException("Board id required");
        }

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new EntityNotFoundException("Board not found"));

        return columnRepository.findColumnsByBoard(board);
    }

    @Override
    @Transactional(readOnly = true)
    public Column findById(Long id) {
        return columnRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Column not found"));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Column column = columnRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Column not found"));

        columnRepository.delete(column);
    }

    @Override
    @Transactional
    public Column update(Column column) {

        Board board = boardRepository.findById(column.getBoard().getId()).orElseThrow(() -> new EntityNotFoundException("Board not found"));

        column.setBoard(board);

        return columnRepository.save(column);
    }
}
