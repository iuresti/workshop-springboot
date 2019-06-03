package org.desarrolladorslp.workshops.springboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.desarrolladorslp.workshops.springboot.forms.BoardForm;
import org.desarrolladorslp.workshops.springboot.models.Board;
import org.desarrolladorslp.workshops.springboot.models.User;
import org.desarrolladorslp.workshops.springboot.services.BoardService;
import org.desarrolladorslp.workshops.springboot.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BoardControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private BoardService boardService;

    @Mock
    private UserService userService;

    @Mock
    private Principal principal;

    @InjectMocks
    private BoardController boardController;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(boardController)
                .setControllerAdvice(new ErrorHandler())
                .build();
    }

    @Test
    public void givenValidForm_whenCreateBoard_thenAcceptAnd200Status() throws Exception {
        // given
        BoardForm boardForm = new BoardForm();
        boardForm.setName("TODO");

        User user01 = new User().builder().id(1L).username("user01").build();

        Board createdBoard = new Board();
        createdBoard.setId(1L);
        createdBoard.setName("TODO");
        createdBoard.setUser(user01);

        BoardForm toPersistForm = new BoardForm();
        toPersistForm.setName("TODO");
        toPersistForm.setUserId(1L);

        given(principal.getName()).willReturn(user01.getUsername());
        given(userService.findByUsername("user01")).willReturn(user01);
        given(boardService.create(any(BoardForm.class))).willReturn(createdBoard);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/board")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(boardForm))
                        .principal(principal)
            ).andReturn().getResponse();

        // then
        verify(principal, atLeast(1)).getName();
        verify(userService).findByUsername("user01");
        verify(boardService).create(toPersistForm);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(createdBoard));

    }

    @Test
    public void givenInvalidForm_whenCreateBoard_thenRejectAnd400Status() throws Exception {
        // given
        BoardForm boardForm = new BoardForm();

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/board")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(boardForm))
                        .principal(principal)
            ).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    public void givenBoardsForUserInStorage_whenRetrieveBoardsForCurrentUser_thenGetDataAnd200Status()
            throws Exception {
        // given
        User user01 = new User().builder().id(1L).username("user01").build();

        Board board01 = new Board();
        board01.setId(1L);
        board01.setName("TODO");
        board01.setUser(user01);

        Board board02 = new Board();
        board02.setId(2L);
        board02.setName("DONE");
        board02.setUser(user01);

        List<Board> userBoards = Arrays.asList(board01, board02);

        given(principal.getName()).willReturn(user01.getUsername());
        given(userService.findByUsername("user01")).willReturn(user01);
        given(boardService.findByUser(user01.getId())).willReturn(userBoards);


        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/board/forCurrentUser")
                        .principal(principal)
        ).andReturn().getResponse();

        // then
        verify(principal, atLeast(1)).getName();
        verify(userService).findByUsername("user01");
        verify(boardService).findByUser(user01.getId());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(userBoards));

    }

    @Test
    public void givenNoBoardsForUserInStorage_whenRetrieveBoardsForCurrentUser_thenGetNoDataAnd200Status()
            throws Exception {
        // given
        User user01 = new User().builder().id(1L).username("user01").build();

        given(principal.getName()).willReturn(user01.getUsername());
        given(userService.findByUsername("user01")).willReturn(user01);
        given(boardService.findByUser(user01.getId())).willReturn(Collections.emptyList());

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/board/forCurrentUser")
                        .principal(principal)
        ).andReturn().getResponse();

        // then
        verify(principal, atLeast(1)).getName();
        verify(userService).findByUsername("user01");
        verify(boardService).findByUser(user01.getId());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(Collections.emptyList()));
    }

    @Test
    public void givenBoardsForUserInStorage_whenRetrieveBoardsForUserId_thenGetDataAnd200Status()
            throws Exception {
        // given
        User user01 = new User().builder().id(1L).username("user01").build();

        Board board01 = new Board();
        board01.setId(1L);
        board01.setName("TODO");
        board01.setUser(user01);

        Board board02 = new Board();
        board02.setId(2L);
        board02.setName("DONE");
        board02.setUser(user01);

        List<Board> userBoards = Arrays.asList(board01, board02);

        given(boardService.findByUser(user01.getId())).willReturn(userBoards);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/board/user/{userId}", user01.getId())
        ).andReturn().getResponse();

        // then
        verify(boardService).findByUser(user01.getId());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(userBoards));

    }

    @Test
    public void givenBoardsForUserInStorage_whenRetrieveBoardsForUserId_thenGetNoDataAnd200Status()
            throws Exception {
        // given
        User user01 = new User().builder().id(1L).username("user01").build();

        given(boardService.findByUser(user01.getId())).willReturn(Collections.emptyList());

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/board/user/{userId}", user01.getId())
        ).andReturn().getResponse();

        // then
        verify(boardService).findByUser(user01.getId());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(Collections.emptyList()));
    }

    @Test
    public void givenBoardInStorage_whenRetrieveBoardForCurrentUser_thenGetDataAnd200Status()
            throws Exception {
        // given
        User user01 = new User().builder().id(1L).username("user01").build();

        Board board01 = new Board();
        board01.setId(1L);
        board01.setName("TODO");
        board01.setUser(user01);

        given(principal.getName()).willReturn(user01.getUsername());
        given(userService.findByUsername("user01")).willReturn(user01);
        given(boardService.findByIdAndUserId(1l, 1L)).willReturn(board01);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/board/{id}", board01.getId())
                .principal(principal)
        )
                .andReturn().getResponse();

        // then
        verify(principal, atLeast(1)).getName();
        verify(userService).findByUsername("user01");
        verify(boardService).findByIdAndUserId(board01.getId(), user01.getId());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(board01));
    }

    @Test
    public void givenNoBoardInStorage_whenRetrieveBoardForCurrentUser_thenAcceptAnd404Status()
            throws Exception {
        // given
        User user01 = new User().builder().id(1L).username("user01").build();

        given(principal.getName()).willReturn(user01.getUsername());
        given(userService.findByUsername("user01")).willReturn(user01);
        given(boardService.findByIdAndUserId(1L, 1L))
                .willThrow(new EntityNotFoundException("Board for User not found"));

        // when
        MockHttpServletResponse response = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/board/{id}", 1L)
                .principal(principal)
        ).andReturn().getResponse();

        // then
        verify(principal, atLeast(1)).getName();
        verify(userService).findByUsername("user01");
        verify(boardService).findByIdAndUserId(1L, user01.getId());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());

    }

    @Test
    public void givenBoardInStorage_whenDeleteBoardForCurrentUser_thenAcceptAnd200Status()
            throws Exception {
        // given
        User user01 = new User().builder().id(1L).username("user01").build();

        Board board01 = new Board();
        board01.setId(1L);
        board01.setName("TODO");
        board01.setUser(user01);

        given(principal.getName()).willReturn(user01.getUsername());
        given(userService.findByUsername("user01")).willReturn(user01);
        given(boardService.findByIdAndUserId(1l, 1L)).willReturn(board01);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/board/{id}", board01.getId())
                        .principal(principal)
        )
                .andReturn().getResponse();

        // then
        verify(principal, atLeast(1)).getName();
        verify(userService).findByUsername("user01");
        verify(boardService).findByIdAndUserId(board01.getId(), user01.getId());
        verify(boardService).deleteById(board01.getId());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

    }

    @Test
    public void givenNoBoardInStorage_whenDeleteBoardForCurrentUser_thenAcceptAnd404Status()
            throws Exception {
        // given
        User user01 = new User().builder().id(1L).username("user01").build();

        given(principal.getName()).willReturn(user01.getUsername());
        given(userService.findByUsername("user01")).willReturn(user01);
        given(boardService.findByIdAndUserId(1L, 1L))
                .willThrow(new EntityNotFoundException("Board for User not found"));

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/board/{id}", 1L)
                        .principal(principal)
        ).andReturn().getResponse();

        // then
        verify(principal, atLeast(1)).getName();
        verify(userService).findByUsername("user01");
        verify(boardService).findByIdAndUserId(1L, user01.getId());
        verify(boardService, atLeast(0)).deleteById(anyLong());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void givenInvalidForm_whenUpdateBoardForCurrentUser_thenRejectAnd400Status()
            throws Exception {
        // given
        BoardForm boardForm = new BoardForm();
        boardForm.setId(1L);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.put("/api/board")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(boardForm))
                .principal(principal)
        )
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void givenBoardInStorageAndValidForm_whenUpdateBoardForCurrentUser_thenAcceptAnd200Status()
            throws Exception {
        // given
        BoardForm boardForm = new BoardForm();
        boardForm.setId(1L);
        boardForm.setName("FINISHED");

        User user01 = new User().builder().id(1L).username("user01").build();

        // This is what is retrieved prior to update
        Board dbBoard01 = new Board();
        dbBoard01.setId(1L);
        dbBoard01.setName("TODO");
        dbBoard01.setUser(user01);

        // This is what is expected after the update
        Board savedBoard01 = new Board();
        savedBoard01.setId(1L);
        savedBoard01.setName("FINISHED");
        savedBoard01.setUser(user01);

        given(principal.getName()).willReturn(user01.getUsername());
        given(userService.findByUsername("user01")).willReturn(user01);
        given(boardService.findByIdAndUserId(1l, 1L)).willReturn(dbBoard01);
        given(boardService.update(boardForm)).willReturn(savedBoard01);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.put("/api/board")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(boardForm))
                        .principal(principal)
        )
                .andReturn().getResponse();

        // then
        verify(principal, atLeast(1)).getName();
        verify(userService).findByUsername("user01");
        verify(boardService).findByIdAndUserId(boardForm.getId(), user01.getId());
        verify(boardService).update(boardForm);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(savedBoard01));

    }

    @Test
    public void givenNoBoardInStorageAndValidForm_whenUpdateBoardForCurrentUser_thenAcceptAnd404Status()
            throws Exception {
        // given
        BoardForm boardForm = new BoardForm();
        boardForm.setId(1L);
        boardForm.setName("FINISHED");

        User user01 = new User().builder().id(1L).username("user01").build();

        given(principal.getName()).willReturn(user01.getUsername());
        given(userService.findByUsername("user01")).willReturn(user01);
        given(boardService.findByIdAndUserId(boardForm.getId(), user01.getId()))
                .willThrow(new EntityNotFoundException("Board for User not found"));

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.put("/api/board")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(boardForm))
                        .principal(principal)
        ).andReturn().getResponse();

        // then
        verify(principal, atLeast(1)).getName();
        verify(userService).findByUsername("user01");
        verify(boardService).findByIdAndUserId(boardForm.getId(), user01.getId());
        verify(boardService, atLeast(0)).update(any(BoardForm.class));

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void givenBoardInStorage_whenDuplicateBoardForCurrentUser_thenAcceptAnd200Status()
            throws Exception {
        // given
        User user01 = new User().builder().id(1L).username("user01").build();

        Board board01 = new Board();
        board01.setId(1L);
        board01.setName("TODO");
        board01.setUser(user01);

        given(principal.getName()).willReturn(user01.getUsername());
        given(userService.findByUsername("user01")).willReturn(user01);
        given(boardService.findByIdAndUserId(1L, user01.getId())).willReturn(board01);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/board/{id}", 1L)
                        .principal(principal)
        ).andReturn().getResponse();

        // then
        verify(principal, atLeast(1)).getName();
        verify(userService).findByUsername("user01");
        verify(boardService).findByIdAndUserId(1L, user01.getId());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.ACCEPTED.value());
    }

    @Test
    public void givenNoBoardInStorage_whenDuplicateBoardForCurrentUser_thenAcceptAnd200Status()
            throws Exception {
        // given
        User user01 = new User().builder().id(1L).username("user01").build();

        given(principal.getName()).willReturn(user01.getUsername());
        given(userService.findByUsername("user01")).willReturn(user01);
        given(boardService.findByIdAndUserId(1L, user01.getId()))
                .willThrow(new EntityNotFoundException("Board for User not found"));

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/board/{id}", 1L)
                        .principal(principal)
        ).andReturn().getResponse();

        // then
        verify(principal, atLeast(1)).getName();
        verify(userService).findByUsername("user01");
        verify(boardService).findByIdAndUserId(1L, user01.getId());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

}
