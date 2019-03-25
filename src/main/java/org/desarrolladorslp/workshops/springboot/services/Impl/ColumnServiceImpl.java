package org.desarrolladorslp.workshops.springboot.services.Impl;

import org.desarrolladorslp.workshops.springboot.models.Board;
import org.desarrolladorslp.workshops.springboot.models.Column;
import org.desarrolladorslp.workshops.springboot.repository.ColumnRepository;
import org.desarrolladorslp.workshops.springboot.services.BoardService;
import org.desarrolladorslp.workshops.springboot.services.ColumnService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColumnServiceImpl implements ColumnService {

    private ColumnRepository columnRepository;
    private BoardService boardService;

    public ColumnServiceImpl(ColumnRepository columnRepository, BoardService boardService){
        this.columnRepository = columnRepository;
        this.boardService = boardService;
    }

    @Override
    public Column createColumn(Column column) {
        return columnRepository.save(column);
    }

    @Override
    public List<Column> findColumnsByBoard(Long boardId) {
        Board board = boardService.findById(boardId);
        return columnRepository.findColumnsByBoard(board);
    }

    @Override
    public Column findById(Long id) {
        return columnRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteColumn(Long id) {
        Column column = columnRepository.findById(id).orElse(null);
        columnRepository.delete(column);
    }

    @Override
    public Column updateColumn(Column column) {
        return columnRepository.save(column);
    }
}
