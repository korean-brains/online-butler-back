package com.koreanbrains.onlinebutlerback.controller.tag;

import com.koreanbrains.onlinebutlerback.common.fixtures.TagFixture;
import com.koreanbrains.onlinebutlerback.common.scroll.Scroll;
import com.koreanbrains.onlinebutlerback.repository.tag.TagQueryRepository;
import com.koreanbrains.onlinebutlerback.repository.tag.TagScrollDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = TagController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
class TagControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TagQueryRepository tagQueryRepository;

    @Test
    @DisplayName("태그를 검색한다")
    void searchTag() throws Exception {
        // given
        Scroll<TagScrollDto> tagScroll = TagFixture.scrollTags(5, 6L, null);
        given(tagQueryRepository.searchTag(anyLong(), anyString(), anyInt()))
                .willReturn(tagScroll);

        // when
        ResultActions result = mockMvc.perform(get("/api/tag")
                .param("cursor", "0")
                .param("tag", "tag")
                .param("size", "5"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.nextCursor").value(6))
                .andExpect(jsonPath("$.content.length()").value(5))
                .andExpect(jsonPath("$.content[0].name").value("tag 1"))
                .andExpect(jsonPath("$.content[1].name").value("tag 2"))
                .andExpect(jsonPath("$.content[2].name").value("tag 3"))
                .andExpect(jsonPath("$.content[3].name").value("tag 4"))
                .andExpect(jsonPath("$.content[4].name").value("tag 5"));
    }



}