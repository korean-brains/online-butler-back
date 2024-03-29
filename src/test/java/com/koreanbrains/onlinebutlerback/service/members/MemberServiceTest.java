package com.koreanbrains.onlinebutlerback.service.members;

import com.koreanbrains.onlinebutlerback.common.exception.EntityNotFoundException;
import com.koreanbrains.onlinebutlerback.common.fixtures.MemberFixture;
import com.koreanbrains.onlinebutlerback.controller.members.MemberCreateRequest;
import com.koreanbrains.onlinebutlerback.controller.members.MemberGetResponse;
import com.koreanbrains.onlinebutlerback.entity.member.Member;
import com.koreanbrains.onlinebutlerback.repository.member.MemberRepository;
import com.koreanbrains.onlinebutlerback.service.member.MemberService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @InjectMocks
    MemberService memberService;
    @Mock
    MemberRepository memberRepository;

    @Test
    @DisplayName("멤버가 생성되는지 확인한다")
    void createMember() {
        // given
        MemberCreateRequest request = new MemberCreateRequest("kim", "kim@gmail.com", "asdfasdf");
        given(memberRepository.save(any())).willReturn(Member.builder().id(1L).build());

        // when
        Long memberId = memberService.createMember(request.name(), request.email(), request.password());

        // then
        assertThat(memberId).isEqualTo(1L);
    }

    @Test
    @DisplayName("멤버가 조회되는지 확인한다")
    void getMember() {
        // given
        Member member = MemberFixture.member();
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        // when
        Member findMember = memberService.getMember(1L);

        // then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getName()).isEqualTo(member.getName());
        assertThat(findMember.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    @DisplayName("조회한 멤버가 없으면 404 예외가 발생한다")
    void failGetMember() {
        // given
        given(memberRepository.findById(any())).willReturn(Optional.empty());

        // when

        // then
        assertThatThrownBy(() -> memberService.getMember(1L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("멤버 이름을 수정한다")
    void updateMember() {
        // given
        Member member = MemberFixture.member();
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        // when
        memberService.updateMember(1L, "changedName");

        // then
        assertThat(member.getName()).isEqualTo("changedName");
    }

    @Test
    @DisplayName("업데이트할 멤버가 없으면 404 예외가 발생한다")
    void failUpdateMember() {
        // given
        given(memberRepository.findById(any())).willReturn(Optional.empty());

        // when

        // then
        assertThatThrownBy(() -> memberService.updateMember(1L, "changedName"))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("멤버를 비활성화 한다")
    void disableMember() {
        // given
        Member member = MemberFixture.member();
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        // when
        memberService.disableMember(1L);

        // then
        assertThat(member.isActive()).isEqualTo(false);
    }

    @Test
    @DisplayName("비활성화할 멤버가 없으면 404 예외가 발생한다")
    void failDisableMember() {
        // given
        given(memberRepository.findById(any())).willReturn(Optional.empty());

        // when

        // then
        assertThatThrownBy(() -> memberService.disableMember(1L))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
