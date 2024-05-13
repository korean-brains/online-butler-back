package com.koreanbrains.onlinebutlerback.controller.follow;

import com.koreanbrains.onlinebutlerback.common.ControllerTest;
import com.koreanbrains.onlinebutlerback.common.context.WithRestMockUser;
import com.koreanbrains.onlinebutlerback.service.follow.FollowService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FollowController.class)
class FollowControllerTest extends ControllerTest {

    @MockBean
    FollowService followService;

    @Test
    @DisplayName("사용자를 팔로우 한다")
    @WithRestMockUser
    void follow() throws Exception {
        // given
        FollowRequest request = new FollowRequest(1L);
        doNothing().when(followService).follow(anyLong(), anyLong());

        // when
        ResultActions result = mockMvc.perform(post("/api/follow")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("사용자를 언팔로우 한다")
    @WithRestMockUser
    void unFollow() throws Exception {
        // given
        UnFollowRequest request = new UnFollowRequest(1L);
        doNothing().when(followService).follow(anyLong(), anyLong());

        // when
        ResultActions result = mockMvc.perform(delete("/api/follow")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isNoContent());
    }

}