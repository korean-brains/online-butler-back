package com.koreanbrains.onlinebutlerback.controller.post;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = PostController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
class PostControllerTest {

    @MockBean
    PostService postService;
    @Autowired
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("포스트를 생성한다")
    void createPost() throws Exception {
        // given
        given(postService.createPost(any(), any())).willReturn(1L);
        PostCreateRequest request = new PostCreateRequest("포스트 내용");
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

}