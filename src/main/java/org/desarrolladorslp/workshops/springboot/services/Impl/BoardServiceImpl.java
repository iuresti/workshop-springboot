package org.desarrolladorslp.workshops.springboot.services.Impl;

import java.util.List;
import java.util.Objects;

import javax.persistence.EntityNotFoundException;

import org.desarrolladorslp.workshops.springboot.models.Board;
import org.desarrolladorslp.workshops.springboot.models.User;
import org.desarrolladorslp.workshops.springboot.repository.BoardRepository;
import org.desarrolladorslp.workshops.springboot.repository.UserRepository;
import org.desarrolladorslp.workshops.springboot.services.BoardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardServiceImpl implements BoardService {

    private BoardRepository boardRepository;
    private UserRepository userRepository;

    public BoardServiceImpl(BoardRepository boardRepository, UserRepository userRepository) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Board create(Board board) {
        if (Objects.isNull(board.getUser())) {
            throw new IllegalArgumentException("User required");
        }
        User user = userRepository.findById(board.getUser().getId()).orElseThrow(() -> new EntityNotFoundException("User not found"));

        board.setUser(user);

        return boardRepository.save(board);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Board> findByUser(Long userId) {

        if (Objects.isNull(userId)) {
            throw new IllegalArgumentException("User id required");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));

        return boardRepository.findBoardsByUser(user);
    }

    @Override
    public Board findById(Long id) {
        return boardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Board not found"));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Board not found"));
        boardRepository.delete(board);
    }

    @Override
    public Board update(Board board) {
        return boardRepository.save(board);
    }
}
