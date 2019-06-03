package org.desarrolladorslp.workshops.springboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.desarrolladorslp.workshops.springboot.controllers.ColumnController;
import org.desarrolladorslp.workshops.springboot.controllers.ErrorHandler;
import org.desarrolladorslp.workshops.springboot.exceptions.ResourceNotFoundForUserException;
import org.desarrolladorslp.workshops.springboot.forms.ColumnForm;
import org.desarrolladorslp.workshops.springboot.models.Column;
import org.desarrolladorslp.workshops.springboot.models.User;
import org.desarrolladorslp.workshops.springboot.services.ColumnService;
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

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ColumnControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private ColumnService columnService;

    @Mock
    private UserService userService;

    @Mock
    private Principal principal;

    @InjectMocks
    private ColumnController columnController;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(columnController)
                .setControllerAdvice(new ErrorHandler())
                .build();
    }

    @Test
    public void it_should_return_http_200_when_given_a_valid_column_form() throws Exception {
        // given
        ColumnForm columnForm = new ColumnForm();
        columnForm.setName("Column01");
        columnForm.setBoardId(1L);

        User user01 = new User().builder().id(1L).username("user01").build();

        Column dbColumn01 = new Column();
        dbColumn01.setId(1L);
        dbColumn01.setName("Column01");

        ColumnForm toPersistForm = new ColumnForm();
        toPersistForm.setBoardId(1L);
        toPersistForm.setName("Column01");

        given(principal.getName()).willReturn(user01.getUsername());
        given(userService.findByUsername("user01")).willReturn(user01);
        given(columnService.createColumnForUser(any(ColumnForm.class), anyLong())).willReturn(dbColumn01);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/column")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(columnForm))
                        .principal(principal)
        ).andReturn().getResponse();

        // then
        verify(principal, atLeast(1)).getName();
        verify(userService).findByUsername("user01");
        verify(columnService).createColumnForUser(toPersistForm, 1L);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(dbColumn01));

    }

    @Test
    public void it_should_return_http_400_when_given_an_invalid_column_form() throws Exception {
        // given
        ColumnForm columnForm = new ColumnForm();

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/column")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(columnForm))
                        .principal(principal)
        ).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void it_should_return_http_200_when_columns_found_for_board_id_and_user() throws Exception {
        // given
        User user01 = new User().builder().id(1L).username("user01").build();

        Column column01 = new Column();
        column01.setName("Column01");
        column01.setId(1L);

        Column column02 = new Column();
        column02.setName("Column02");
        column02.setId(1L);

        List<Column> userColumns = Arrays.asList(column01, column02);

        given(principal.getName()).willReturn(user01.getUsername());
        given(userService.findByUsername("user01")).willReturn(user01);
        given(columnService.findColumnsByBoardForUser(1L, 1L)).willReturn(userColumns);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/column/board/{boardId}", 1L)
                        .principal(principal)
        ).andReturn().getResponse();

        // then
        verify(principal, atLeast(1)).getName();
        verify(userService).findByUsername("user01");
        verify(columnService).findColumnsByBoardForUser(1L, user01.getId());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(userColumns));

    }

    @Test
    public void it_should_return_http_200_when_no_columns_found_for_board_id_and_user() throws Exception {
        // given
        User user01 = new User().builder().id(1L).username("user01").build();

        given(principal.getName()).willReturn(user01.getUsername());
        given(userService.findByUsername("user01")).willReturn(user01);
        given(columnService.findColumnsByBoardForUser(1L, user01.getId()))
                .willReturn(Collections.emptyList());

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/column/board/{boardId}", 1L)
                        .principal(principal)
        ).andReturn().getResponse();

        // then
        verify(principal, atLeast(1)).getName();
        verify(userService).findByUsername("user01");
        verify(columnService).findColumnsByBoardForUser(1L, user01.getId());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(Collections.emptyList()));
    }

    @Test
    public void it_should_return_http_200_when_getting_column_found_for_user() throws Exception {
        // given
        User user01 = new User().builder().id(1L).username("user01").build();

        Column column01 = new Column();
        column01.setName("Column01");
        column01.setId(1L);

        given(principal.getName()).willReturn(user01.getUsername());
        given(userService.findByUsername("user01")).willReturn(user01);
        given(columnService.findColumnForUser(1L, 1L)).willReturn(column01);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/column/{id}", column01.getId())
                        .principal(principal)
        )
                .andReturn().getResponse();

        // then
        verify(principal, atLeast(1)).getName();
        verify(userService).findByUsername("user01");
        verify(columnService).findColumnForUser(column01.getId(), user01.getId());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(column01));
    }

    @Test
    public void it_should_return_http_409_when_getting_column_not_found_for_user() throws Exception {
        // given
        User user01 = new User().builder().id(1L).username("user01").build();

        given(principal.getName()).willReturn(user01.getUsername());
        given(userService.findByUsername("user01")).willReturn(user01);
        given(columnService.findColumnForUser(1L, 1L))
                .willThrow(new ResourceNotFoundForUserException(
                        String.format("Column #%d not found for User #%d", 1L, 1L)));

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/column/{id}", 1L)
                        .principal(principal)
        ).andReturn().getResponse();

        // then
        verify(principal, atLeast(1)).getName();
        verify(userService).findByUsername("user01");
        verify(columnService).findColumnForUser(1L, user01.getId());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    public void it_should_return_http_200_when_deleting_column_found_for_user() throws Exception {
        // given
        User user01 = new User().builder().id(1L).username("user01").build();

        Column column01 = new Column();
        column01.setName("Column01");
        column01.setId(1L);

        given(principal.getName()).willReturn(user01.getUsername());
        given(userService.findByUsername("user01")).willReturn(user01);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/column/{id}", column01.getId())
                        .principal(principal)
        )
                .andReturn().getResponse();

        // then
        verify(principal, atLeast(1)).getName();
        verify(userService).findByUsername("user01");
        verify(columnService).deleteColumnForUser(column01.getId(), user01.getId());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

    }

    @Test
    public void it_should_return_http_409_when_deleting_column_not_found_for_user() throws Exception {
        // given
        User user01 = new User().builder().id(1L).username("user01").build();

        given(principal.getName()).willReturn(user01.getUsername());
        given(userService.findByUsername("user01")).willReturn(user01);

        willThrow(new ResourceNotFoundForUserException(String.format(
                "Column #%d not found for User #%d", 1L, 1L)))
                .given(columnService)
                .deleteColumnForUser(1L, user01.getId());

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/column/{id}", 1L)
                        .principal(principal)
        ).andReturn().getResponse();

        // then
        verify(principal, atLeast(1)).getName();
        verify(userService).findByUsername("user01");
        verify(columnService).deleteColumnForUser(1L, user01.getId());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    public void it_should_return_http_400_when_updating_column_with_an_invalid_column_form_for_user() throws Exception {
        // given
        ColumnForm columnForm = new ColumnForm();

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.put("/api/column")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(columnForm))
                        .principal(principal)
        )
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void it_should_return_http_200_when_updating_column_with_a_valid_column_form_for_user() throws Exception {
        // given
        ColumnForm columnForm = new ColumnForm();
        columnForm.setBoardId(1L);
        columnForm.setName("Column01");
        columnForm.setId(1L);

        User user01 = new User().builder().id(1L).username("user01").build();

        // This is what is retrieved prior to update
        Column dbColumn01 = new Column();
        dbColumn01.setName("Column01");
        dbColumn01.setId(1L);

        // This is what is expected after the update
        Column updatedColumn01 = new Column();
        updatedColumn01.setName("updatedColumn01");
        updatedColumn01.setId(1L);

        given(principal.getName()).willReturn(user01.getUsername());
        given(userService.findByUsername("user01")).willReturn(user01);
        given(columnService.updateColumnForUser(columnForm, user01.getId())).willReturn(updatedColumn01);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.put("/api/column")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(columnForm))
                        .principal(principal)
        )
                .andReturn().getResponse();

        // then
        verify(principal, atLeast(1)).getName();
        verify(userService).findByUsername("user01");
        verify(columnService).updateColumnForUser(columnForm, user01.getId());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(updatedColumn01));

    }

    @Test
    public void it_should_return_http_409_when_updating_column_with_column_id_not_associated_to_user() throws Exception {
        // given
        ColumnForm columnForm = new ColumnForm();
        columnForm.setBoardId(1L);
        columnForm.setName("Column01");
        columnForm.setId(1L);

        User user01 = new User().builder().id(1L).username("user01").build();

        given(principal.getName()).willReturn(user01.getUsername());
        given(userService.findByUsername("user01")).willReturn(user01);
        given(columnService.updateColumnForUser(columnForm, user01.getId()))
                .willThrow(new ResourceNotFoundForUserException(
                        String.format("Column #%d not found for User #%d", 1L, 1L)));

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.put("/api/column")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(columnForm))
                        .principal(principal)
        ).andReturn().getResponse();

        // then
        verify(principal, atLeast(1)).getName();
        verify(userService).findByUsername("user01");
        verify(columnService).updateColumnForUser(columnForm, user01.getId());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
    }
}
