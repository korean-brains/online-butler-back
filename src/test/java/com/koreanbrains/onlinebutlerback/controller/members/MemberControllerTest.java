package com.koreanbrains.onlinebutlerback.controller.members;

import com.koreanbrains.onlinebutlerback.common.ControllerTest;
import com.koreanbrains.onlinebutlerback.common.context.WithRestMockUser;
import com.koreanbrains.onlinebutlerback.common.exception.EntityNotFoundException;
import com.koreanbrains.onlinebutlerback.common.exception.ErrorCode;
import com.koreanbrains.onlinebutlerback.common.fixtures.FileFixture;
import com.koreanbrains.onlinebutlerback.common.fixtures.MemberFixture;
import com.koreanbrains.onlinebutlerback.entity.member.Member;
import com.koreanbrains.onlinebutlerback.repository.member.MemberRepository;
import com.koreanbrains.onlinebutlerback.service.member.MemberService;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MemberController.class)
class MemberControllerTest extends ControllerTest {

    @MockBean
    MemberService memberService;
    @MockBean
    MemberRepository memberRepository;

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
        Member member = MemberFixture.member();
        given(memberService.getMember(anyLong())).willReturn(member);

        // when
        ResultActions result = mockMvc.perform(get("/api/member/{memberId}", 1));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(member.getId()))
                .andExpect(jsonPath("$.name").value(member.getName()))
                .andExpect(jsonPath("$.email").value(member.getEmail()));
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
    @DisplayName("멤버 이름을 수정한다.")
    void updateMember() throws Exception {
        // given
        given(memberService.updateMember(anyLong(), anyString())).willReturn(1L);
        MemberUpdateRequest request = new MemberUpdateRequest("lee");

        // when
        ResultActions result = mockMvc.perform(patch("/api/member/{memberId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().string("1"));
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
    @DisplayName("프로필 이미지를 변경한다")
    @WithRestMockUser
    void updateProfileImage() throws Exception {
        // given
        MockMultipartFile image = FileFixture.multipartImage("profileImage");
        given(memberService.updateProfileImage(anyLong(), any())).willReturn("assets/image.jpg");

        // when
        ResultActions result = mockMvc.perform(multipart("/api/member/me/profile-image")
                .file(image));

        // then
        result.andExpect(status().isOk());
    }
}
