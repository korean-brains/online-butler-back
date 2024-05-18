package com.koreanbrains.onlinebutlerback.controller.comment;

import com.koreanbrains.onlinebutlerback.common.ControllerTest;
import com.koreanbrains.onlinebutlerback.common.context.WithRestMockUser;
import com.koreanbrains.onlinebutlerback.common.scroll.Scroll;
import com.koreanbrains.onlinebutlerback.repository.comment.CommentQueryRepository;
import com.koreanbrains.onlinebutlerback.repository.comment.ReplyScrollDto;
import com.koreanbrains.onlinebutlerback.service.comment.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = CommentController.class)
class CommentControllerTest extends ControllerTest {

    @MockBean
    CommentService commentService;
    @MockBean
    CommentQueryRepository commentQueryRepository;


    @Test
    @DisplayName("댓글을 작성한다")
    @WithRestMockUser
    void writeComment() throws Exception {
        // given
        given(commentService.writeComment(anyLong(), anyLong(), anyString())).willReturn(1L);
        CommentWriteRequest request = new CommentWriteRequest(1L, "댓글 내용");

        // when
        ResultActions result = mockMvc.perform(post("/api/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)));

        // then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.commentId").value(1L));
    }

    @Test
    @DisplayName("댓글을 삭제한다")
    @WithRestMockUser
    void deleteComment() throws Exception {
        // given
        doNothing().when(commentService).deleteComment(anyLong(), anyLong());

        // when
        ResultActions result = mockMvc.perform(delete("/api/comment/{commentId}", 1));

        // then
        result.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("답글을 작성한다")
    @WithRestMockUser
    void writeReply() throws Exception {
        // given
        given(commentService.writeReply(anyLong(), anyLong(), anyString())).willReturn(1L);
        ReplyWriteRequest request = new ReplyWriteRequest("답글 내용");

        // when
        ResultActions result = mockMvc.perform(post("/api/comment/{commentId}/reply", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)));

        // then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.commentId").value(1L));
    }

    @Test
    @DisplayName("답글 목록을 무한스크롤로 조회한다")
    void scrollReply() throws Exception {
        // given
        List<ReplyScrollDto> content = List.of(
                new ReplyScrollDto(1L, "답글 내용", "kim", "profile image", "lee", "profile image", LocalDateTime.of(2024, 2, 1, 10, 0), 1L),
                new ReplyScrollDto(2L, "답글 내용", "kim", "profile image", "lee", "profile image", LocalDateTime.of(2024, 2, 1, 10, 1), 1L),
                new ReplyScrollDto(3L, "답글 내용", "kim", "profile image", "lee", "profile image", LocalDateTime.of(2024, 2, 1, 10, 2), 1L),
                new ReplyScrollDto(4L, "답글 내용", "kim", "profile image", "lee", "profile image", LocalDateTime.of(2024, 2, 1, 10, 3), 1L),
                new ReplyScrollDto(5L, "답글 내용", "kim", "profile image", "lee", "profile image", LocalDateTime.of(2024, 2, 1, 10, 4), 1L)
        );
        given(commentQueryRepository.scrollReply(anyLong(), anyLong(), anyInt()))
                .willReturn(new Scroll<>(content, 6L, null));

        // when
        ResultActions result = mockMvc.perform(get("/api/comment/{commentId}/reply", 1)
                .param("cursor", "0")
                .param("size", "5"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.nextCursor").value(6L))
                .andExpect(jsonPath("$.nextSubCursor").isEmpty())
                .andExpect(jsonPath("$.content.length()").value(5))
                .andExpect(jsonPath("$.content[0].id").exists())
                .andExpect(jsonPath("$.content[0].text").exists())
                .andExpect(jsonPath("$.content[0].author").exists())
                .andExpect(jsonPath("$.content[0].profile").exists())
                .andExpect(jsonPath("$.content[0].parentAuthor").exists())
                .andExpect(jsonPath("$.content[0].parentProfile").exists())
                .andExpect(jsonPath("$.content[0].createdAt").exists());
    }

}