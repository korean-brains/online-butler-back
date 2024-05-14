package com.koreanbrains.onlinebutlerback.controller.members;

import com.koreanbrains.onlinebutlerback.common.ControllerTest;
import com.koreanbrains.onlinebutlerback.common.context.WithRestMockUser;
import com.koreanbrains.onlinebutlerback.common.exception.EntityNotFoundException;
import com.koreanbrains.onlinebutlerback.common.exception.ErrorCode;
import com.koreanbrains.onlinebutlerback.common.fixtures.FileFixture;
import com.koreanbrains.onlinebutlerback.common.fixtures.FollowFixture;
import com.koreanbrains.onlinebutlerback.common.fixtures.MemberFixture;
import com.koreanbrains.onlinebutlerback.common.fixtures.PostFixture;
import com.koreanbrains.onlinebutlerback.common.scroll.Scroll;
import com.koreanbrains.onlinebutlerback.repository.follow.FollowDto;
import com.koreanbrains.onlinebutlerback.repository.follow.FollowQueryRepository;
import com.koreanbrains.onlinebutlerback.repository.member.MemberDto;
import com.koreanbrains.onlinebutlerback.repository.member.MemberQueryRepository;
import com.koreanbrains.onlinebutlerback.repository.member.MemberRepository;
import com.koreanbrains.onlinebutlerback.repository.post.PostQueryRepository;
import com.koreanbrains.onlinebutlerback.repository.post.PostScrollDto;
import com.koreanbrains.onlinebutlerback.service.member.MemberService;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MemberController.class)
class MemberControllerTest extends ControllerTest {

    @MockBean
    MemberService memberService;
    @MockBean
    MemberRepository memberRepository;
    @MockBean
    MemberQueryRepository memberQueryRepository;
    @MockBean
    PostQueryRepository postQueryRepository;
    @MockBean
    FollowQueryRepository followQueryRepository;

    @Test
    @DisplayName("멤버를 생성한다")
    void createMember() throws Exception {
        // given
        given(memberService.createMember(anyString(), anyString(), anyString())).willReturn(1L);
        MemberCreateRequest request = new MemberCreateRequest("kim", "kin@gmail.com", "password");

        // when
        ResultActions result = mockMvc.perform(post("/api/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)));

        // then
        result.andExpect(status().isCreated())
                .andExpect(content().string("1"));
    }

    @Test
    @DisplayName("멤버를 조회한다")
    void getMember() throws Exception {
        // given
        MemberDto member = MemberFixture.memberDto();
        given(memberQueryRepository.findById(anyLong())).willReturn(Optional.of(member));

        // when
        ResultActions result = mockMvc.perform(get("/api/member/{memberId}", 1));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(member.id()))
                .andExpect(jsonPath("$.name").value(member.name()))
                .andExpect(jsonPath("$.email").value(member.email()))
                .andExpect(jsonPath("$.profileImage").value(member.profileImage()))
                .andExpect(jsonPath("$.postCount").value(member.postCount()))
                .andExpect(jsonPath("$.followerCount").value(member.followerCount()))
                .andExpect(jsonPath("$.followingCount").value(member.followingCount()));
    }

    @Test
    @DisplayName("존재하지 않는 멤버를 조회하면 404를 반환한다")
    void failGetMember() throws Exception {
        // given
        given(memberService.getMember(anyLong())).willThrow(new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        // when
        ResultActions result = mockMvc.perform(get("/api/member/{memberId}", 1));

        // then
        result.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("멤버 프로필을 수정한다.")
    void updateMember() throws Exception {
        // given
        doNothing().when(memberService).updateMember(anyLong(), anyString(), anyString(), any());
        MemberUpdateRequest request = new MemberUpdateRequest("lee", "hello", FileFixture.multipartImage("profileImage"));

        // when
        ResultActions result = mockMvc.perform(multipart("/api/member/{memberId}", 1)
                .file((MockMultipartFile) request.profileImage())
                .param("name", request.name())
                .param("introduction", request.introduction()));

        // then
        result.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("멤버를 비활성화 한다.")
    void disableMember() throws Exception {
        // given
        given(memberService.disableMember(anyLong())).willReturn(1L);

        // when
        ResultActions result = mockMvc.perform(delete("/api/member/{memberId}", 1));

        // then
        result.andExpect(status().isNoContent())
                .andExpect(content().string("1"));
    }

    @Test
    @DisplayName("내 정보를 조회한다")
    @WithRestMockUser
    void findMe() throws Exception {
        // given
        MemberDto memberDto = MemberFixture.memberDto();
        given(memberQueryRepository.findById(anyLong())).willReturn(Optional.of(memberDto));

        // when
        ResultActions result = mockMvc.perform(get("/api/member/me"));

        // then
        result.andExpect(status().isOk());
    }

    @Test
    @DisplayName("내 정보가 조회되지 않으면 404를 반환한다.")
    @WithRestMockUser
    void findMeFailNotFound() throws Exception {
        // given
        given(memberQueryRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        ResultActions result = mockMvc.perform(get("/api/member/me"));

        // then
        result.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("특정 사용자가 작성한 게시글 목록을 조회한다.")
    void scrollPostWriter() throws Exception {
        // given

        List<PostScrollDto> content = PostFixture.scrollPost(10L, 7L);
        given(postQueryRepository.scrollPost(anyLong(), anyInt(), anyLong()))
                .willReturn(new Scroll<>(content, 6L, null));

        // when
        ResultActions result = mockMvc.perform(get("/api/member/{memberId}/post", 1)
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
    @DisplayName("팔로우 목록을 조회한다.")
    void getFollowList() throws Exception {
        // given
        List<FollowDto> content = FollowFixture.followList(1L, 5L);
        given(followQueryRepository.findFollowingList(anyLong(), anyLong(), anyInt()))
                .willReturn(new Scroll<>(content, 6L, null));

        // when
        ResultActions result = mockMvc.perform(get("/api/member/{memberId}/following", 1)
                .param("cursor", "1")
                .param("size", "5"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.nextCursor").value(6))
                .andExpect(jsonPath("$.nextSubCursor").isEmpty())
                .andExpect(jsonPath("$.content.length()").value(5))
                .andExpect(jsonPath("$.content[0].id").exists())
                .andExpect(jsonPath("$.content[0].memberId").exists())
                .andExpect(jsonPath("$.content[0].name").exists())
                .andExpect(jsonPath("$.content[0].profileImage").exists());
    }

    @Test
    @DisplayName("팔로워 목록을 조회한다.")
    void getFollowerList() throws Exception {
        // given
        List<FollowDto> content = FollowFixture.followList(1L, 5L);
        given(followQueryRepository.findFollowerList(anyLong(), anyLong(), anyInt()))
                .willReturn(new Scroll<>(content, 6L, null));

        // when
        ResultActions result = mockMvc.perform(get("/api/member/{memberId}/follower", 1)
                .param("cursor", "1")
                .param("size", "5"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.nextCursor").value(6))
                .andExpect(jsonPath("$.nextSubCursor").isEmpty())
                .andExpect(jsonPath("$.content.length()").value(5))
                .andExpect(jsonPath("$.content[0].id").exists())
                .andExpect(jsonPath("$.content[0].memberId").exists())
                .andExpect(jsonPath("$.content[0].name").exists())
                .andExpect(jsonPath("$.content[0].profileImage").exists());
    }

}
