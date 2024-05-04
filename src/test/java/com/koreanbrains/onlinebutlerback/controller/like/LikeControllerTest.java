package com.koreanbrains.onlinebutlerback.controller.like;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koreanbrains.onlinebutlerback.service.like.LikeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = LikeController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class LikeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    LikeService likeService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("포스트에 좋아요를 누른다")
    void likePost() throws Exception {
        // given
        doNothing().when(likeService).likePost(anyLong(), anyLong());
        LikePostRequest request = new LikePostRequest(1L);

        // when
        ResultActions result = mockMvc.perform(post("/api/like")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)));

        // then
        result.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("포스트 좋아요를 취소한다")
    void dislikePost() throws Exception {
        // given
        doNothing().when(likeService).dislikePost(anyLong(), anyLong());
        DislikePostRequest request = new DislikePostRequest(1L);

        // when
        ResultActions result = mockMvc.perform(delete("/api/like")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)));

        // then
        result.andExpect(status().isNoContent());
    }

}