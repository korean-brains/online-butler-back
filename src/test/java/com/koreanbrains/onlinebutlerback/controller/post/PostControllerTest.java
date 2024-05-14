package com.koreanbrains.onlinebutlerback.controller.post;

import com.koreanbrains.onlinebutlerback.common.ControllerTest;
import com.koreanbrains.onlinebutlerback.common.context.WithRestMockUser;
import com.koreanbrains.onlinebutlerback.common.fixtures.PostFixture;
import com.koreanbrains.onlinebutlerback.common.scroll.Scroll;
import com.koreanbrains.onlinebutlerback.repository.comment.CommentQueryRepository;
import com.koreanbrains.onlinebutlerback.repository.comment.CommentScrollDto;
import com.koreanbrains.onlinebutlerback.repository.post.*;
import com.koreanbrains.onlinebutlerback.repository.tag.TagMappingRepository;
import com.koreanbrains.onlinebutlerback.service.post.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = PostController.class)
class PostControllerTest extends ControllerTest {

    @MockBean
    PostService postService;
    @MockBean
    PostRepository postRepository;
    @MockBean
    PostImageRepository postImageRepository;
    @MockBean
    TagMappingRepository tagMappingRepository;
    @MockBean
    PostQueryRepository postQueryRepository;
    @MockBean
    CommentQueryRepository commentQueryRepository;

    @Test
    @DisplayName("포스트를 생성한다")
    @WithRestMockUser
    void createPost() throws Exception {
        // given
        given(postService.createPost(any(), any(), any(), any())).willReturn(1L);
        MockMultipartFile images = new MockMultipartFile("images",
                "cat.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "<<image>>".getBytes());

        // when
        ResultActions result = mockMvc.perform(multipart("/api/post")
                .file(images)
                .param("caption", "포스트 내용")
                .param("tags", "뚱냥이", "고양이"));

        // then
        result.andExpect(status().isCreated())
                .andExpect(content().string("1"));
    }

    @Test
    @DisplayName("포스트를 조회한다")
    @WithRestMockUser
    void getPost() throws Exception {
        // given
        PostDto postDto = PostFixture.postDto();
        given(postQueryRepository.findById(anyLong(), anyLong())).willReturn(Optional.of(postDto));

        // when
        ResultActions result = mockMvc.perform(get("/api/post/{postId}", 1));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(postDto.getId()))
                .andExpect(jsonPath("$.caption").value(postDto.getCaption()))
                .andExpect(jsonPath("$.images[0]").value(postDto.getImages().get(0)))
                .andExpect(jsonPath("$.tags[0]").value(postDto.getTags().get(0)));
    }

    @Test
    @DisplayName("존재하지 않는 포스트를 조회하면 404를 반환한다")
    @WithRestMockUser
    void failGetPost() throws Exception {
        // given
        given(postRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        ResultActions result = mockMvc.perform(get("/api/post/{postId}", 1));

        // then
        result.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("포스트 내용을 수정한다")
    @WithRestMockUser
    void updatePost() throws Exception {
        // given
        doNothing().when(postService).updatePost(anyLong(), anyString(), any(), anyLong());
        PostUpdateRequest request = new PostUpdateRequest("수정된 포스트 내용", new String[]{"뚱냥이", "고양이"});

        // when
        ResultActions result = mockMvc.perform(patch("/api/post/{postId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)));

        // then
        result.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("포스트를 삭제한다")
    @WithRestMockUser
    void deletePost() throws Exception {
        // given
        doNothing().when(postService).deletePost(anyLong(), anyLong());

        // when
        ResultActions result = mockMvc.perform(delete("/api/post/{postId}", 1));

        // then
        result.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("포스트 목록을 무한스크롤로 조회한다")
    @WithRestMockUser
    void scrollPost() throws Exception {
        // given
        List<PostScrollDto> content = PostFixture.scrollPost(10L, 7L);
        given(postQueryRepository.scrollPost(anyLong(), anyLong(), anyString(), anyInt()))
                .willReturn(new Scroll<>(content, 6L, null));

        // when
        ResultActions result = mockMvc.perform(get("/api/post")
                .param("cursor", "11")
                .param("size", "4")
                .param("tagName", ""));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.nextCursor").value(6))
                .andExpect(jsonPath("$.nextSubCursor").isEmpty())
                .andExpect(jsonPath("$.content.length()").value(4))
                .andExpect(jsonPath("$.content[0].id").exists())
                .andExpect(jsonPath("$.content[0].caption").exists())
                .andExpect(jsonPath("$.content[0].tags").exists());
    }

    @Test
    @DisplayName("댓글 목록을 무한스크롤로 조회한다")
    void scrollComment() throws Exception {
        // given
        List<CommentScrollDto> content = List.of(
                new CommentScrollDto(1L, "포스트 내용", "kim", "profile image", LocalDateTime.of(2024, 2, 1, 10, 0)),
                new CommentScrollDto(2L, "포스트 내용", "kim", "profile image", LocalDateTime.of(2024, 2, 1, 10, 1)),
                new CommentScrollDto(3L, "포스트 내용", "kim", "profile image", LocalDateTime.of(2024, 2, 1, 10, 2)),
                new CommentScrollDto(4L, "포스트 내용", "kim", "profile image", LocalDateTime.of(2024, 2, 1, 10, 3))
        );
        given(commentQueryRepository.scrollComment(anyLong(), anyLong(), anyInt()))
                .willReturn(new Scroll<>(content, 5L, null));

        // when
        ResultActions result = mockMvc.perform(get("/api/post/{postId}/comment", 1)
                .param("cursor", "0")
                .param("size", "4"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.nextCursor").value(5))
                .andExpect(jsonPath("$.nextSubCursor").isEmpty())
                .andExpect(jsonPath("$.content.length()").value(4))
                .andExpect(jsonPath("$.content[0].id").exists())
                .andExpect(jsonPath("$.content[0].text").exists())
                .andExpect(jsonPath("$.content[0].author").exists())
                .andExpect(jsonPath("$.content[0].profile").exists())
                .andExpect(jsonPath("$.content[0].createdAt").exists());
    }

    @Test
    @DisplayName("좋아요한 게시글을 무한스크롤로 조회한다")
    @WithRestMockUser
    void scrollLikePost() throws Exception {
        // given
        List<PostScrollDto> content = PostFixture.scrollPost(10L, 7L);
        given(postQueryRepository.scrollLikePost(anyLong(), anyLong(), anyLong(), anyInt()))
                .willReturn(new Scroll<>(content, 6L, null));

        // when
        ResultActions result = mockMvc.perform(get("/api/post/like")
                .param("cursor", "11")
                .param("size", "4"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.nextCursor").value(6))
                .andExpect(jsonPath("$.nextSubCursor").isEmpty())
                .andExpect(jsonPath("$.content.length()").value(4))
                .andExpect(jsonPath("$.content[0].id").exists())
                .andExpect(jsonPath("$.content[0].caption").exists());
    }

}