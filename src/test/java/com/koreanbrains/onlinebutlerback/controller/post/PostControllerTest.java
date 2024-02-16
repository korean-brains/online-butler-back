package com.koreanbrains.onlinebutlerback.controller.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koreanbrains.onlinebutlerback.common.fixtures.PostFixture;
import com.koreanbrains.onlinebutlerback.common.fixtures.PostImageFixture;
import com.koreanbrains.onlinebutlerback.common.fixtures.TagFixture;
import com.koreanbrains.onlinebutlerback.common.fixtures.TagMappingFixture;
import com.koreanbrains.onlinebutlerback.entity.post.Post;
import com.koreanbrains.onlinebutlerback.entity.post.PostImage;
import com.koreanbrains.onlinebutlerback.entity.tag.Tag;
import com.koreanbrains.onlinebutlerback.entity.tag.TagMapping;
import com.koreanbrains.onlinebutlerback.repository.post.PostImageRepository;
import com.koreanbrains.onlinebutlerback.repository.post.PostRepository;
import com.koreanbrains.onlinebutlerback.repository.tag.TagMappingRepository;
import com.koreanbrains.onlinebutlerback.service.post.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = PostController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
class PostControllerTest {

    @MockBean
    PostService postService;
    @MockBean
    PostRepository postRepository;
    @MockBean
    PostImageRepository postImageRepository;
    @MockBean
    TagMappingRepository tagMappingRepository;
    @Autowired
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("포스트를 생성한다")
    void createPost() throws Exception {
        // given
        given(postService.createPost(any(), any(), any())).willReturn(1L);
        PostCreateRequest request = new PostCreateRequest("포스트 내용", new String[]{"뚱냥이", "고양이"});
        MockMultipartFile post = new MockMultipartFile("post",
                "post.json",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(request));
        MockMultipartFile images = new MockMultipartFile("images",
                "cat.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "<<image>>".getBytes());

        // when
        ResultActions result = mockMvc.perform(multipart("/post")
                .file(post)
                .file(images));

        // then
        result.andExpect(status().isCreated())
                .andExpect(content().string("1"));
    }

    @Test
    @DisplayName("포스트를 조회한다")
    void getPost() throws Exception {
        // given
        Post post = PostFixture.post();
        List<PostImage> postImages = List.of(
                PostImageFixture.postImage(1L, post),
                PostImageFixture.postImage(2L, post),
                PostImageFixture.postImage(3L, post)
        );
        Tag tag = TagFixture.tag();
        TagMapping tagMapping = TagMappingFixture.tagMapping(post, tag);
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(postImageRepository.findByPostId(anyLong())).willReturn(postImages);
        given(tagMappingRepository.findAllByPost(any())).willReturn(List.of(tagMapping));


        // when
        ResultActions result = mockMvc.perform(get("/post/{postId}", 1));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.caption").value(post.getCaption()))
                .andExpect(jsonPath("$.images[0]").value(postImages.get(0).getUrl()))
                .andExpect(jsonPath("$.images[1]").value(postImages.get(1).getUrl()))
                .andExpect(jsonPath("$.images[2]").value(postImages.get(2).getUrl()))
                .andExpect(jsonPath("$.tags[0]").value(tag.getName()));
    }

    @Test
    @DisplayName("존재하지 않는 포스트를 조회하면 404를 반환한다")
    void failGetPost() throws Exception {
        // given
        given(postRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        ResultActions result = mockMvc.perform(get("/post/{postId}", 1));

        // then
        result.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("포스트 내용을 수정한다")
    void updatePost() throws Exception {
        // given
        doNothing().when(postService).updatePost(anyLong(), anyString(), any(), anyLong());
        PostUpdateRequest request = new PostUpdateRequest("수정된 포스트 내용", new String[]{"뚱냥이", "고양이"});

        // when
        ResultActions result = mockMvc.perform(patch("/post/{postId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)));

        // then
        result.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("포스트를 삭제한다")
    void deletePost() throws Exception {
        // given
        doNothing().when(postService).deletePost(anyLong(), anyLong());

        // when
        ResultActions result = mockMvc.perform(delete("/post/{postId}", 1));

        // then
        result.andExpect(status().isNoContent());
    }

}