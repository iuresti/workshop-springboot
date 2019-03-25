package org.desarrolladorslp.workshops.springboot.services.Impl;

import org.desarrolladorslp.workshops.springboot.models.Board;
import org.desarrolladorslp.workshops.springboot.models.User;
import org.desarrolladorslp.workshops.springboot.repository.BoardRepository;
import org.desarrolladorslp.workshops.springboot.services.BoardService;
import org.desarrolladorslp.workshops.springboot.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {

    private BoardRepository boardRepository;
    private UserService userService;

    public BoardServiceImpl(BoardRepository boardRepository,UserService userService){
        this.boardRepository = boardRepository;
        this.userService = userService;
    }

    @Override
    public Board createBoard(Board board) {
        return boardRepository.save(board);
    }

    @Override
    public List<Board> findBoardsByUser(Long userId) throws Exception {
        User user = userService.findById(userId);
        return boardRepository.findBoardsByUser(user);
    }

    @Override
    public Board findById(Long id) {
        return boardRepository.findBoardById(id);
    }

    @Override
    public void deleteBoard(Long id) throws Exception {
        Board board = boardRepository.findById(id).orElseThrow(()->new Exception("Board not found"));
        boardRepository.delete(board);
    }

    @Override
    public Board updateBoard(Board board) {
        return boardRepository.save(board);
    }
}
