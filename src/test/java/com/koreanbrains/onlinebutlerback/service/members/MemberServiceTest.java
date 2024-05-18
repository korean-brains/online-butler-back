package com.koreanbrains.onlinebutlerback.service.members;

import com.koreanbrains.onlinebutlerback.common.exception.AuthenticationException;
import com.koreanbrains.onlinebutlerback.common.exception.EntityNotFoundException;
import com.koreanbrains.onlinebutlerback.common.fixtures.FileFixture;
import com.koreanbrains.onlinebutlerback.common.fixtures.MemberFixture;
import com.koreanbrains.onlinebutlerback.common.util.file.FileStore;
import com.koreanbrains.onlinebutlerback.common.util.file.UploadFile;
import com.koreanbrains.onlinebutlerback.controller.members.MemberCreateRequest;
import com.koreanbrains.onlinebutlerback.entity.member.Member;
import com.koreanbrains.onlinebutlerback.repository.member.MemberRepository;
import com.koreanbrains.onlinebutlerback.service.member.MemberService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @InjectMocks
    MemberService memberService;
    @Mock
    MemberRepository memberRepository;
    @Mock
    FileStore fileStore;

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
    @DisplayName("이미 사용중인 ID로 회원가입 할수 없다.")
    void createMemberFailDuplicateEmail() {
        // given
        MemberCreateRequest request = new MemberCreateRequest("kim", "kim@gmail.com", "asdfasdf");
        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(MemberFixture.member()));

        // when
        // then
        assertThatThrownBy(() -> memberService.createMember(request.name(), request.email(), request.password()))
                .isInstanceOf(AuthenticationException.class);
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
    @DisplayName("멤버 프로필을 수정한다")
    void updateMember() {
        // given
        Member member = MemberFixture.member();
        String name = "changedName";
        String introduction = "changedIntroduction";
        MockMultipartFile profileImage = FileFixture.multipartImage("profileImage");
        UploadFile uploadFile = FileFixture.uploadFile();

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(fileStore.upload(any(), anyString())).willReturn(uploadFile);
        given(fileStore.hasFile(any())).willReturn(true);

        // when
        memberService.updateMember(1L, name, introduction, profileImage);

        // then
        assertThat(member.getName()).isEqualTo("changedName");
        assertThat(member.getIntroduction()).isEqualTo("changedIntroduction");
        assertThat(member.getProfileImage().getUrl()).isEqualTo(uploadFile.url());
    }

    @Test
    @DisplayName("프로필 이미지 변경시 기존 이미지는 삭제한다")
    void updateProfileDeleteOld() {
        // given
        Member member = MemberFixture.memberWithProfileImage();
        String name = "changedName";
        String introduction = "changedIntroduction";
        MockMultipartFile profileImage = FileFixture.multipartImage("profileImage");
        UploadFile uploadFile = FileFixture.uploadFile();
        String deleteImage = member.getProfileImage().getStoreFilename();

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(fileStore.upload(any(), anyString())).willReturn(uploadFile);
        given(fileStore.hasFile(any())).willReturn(true);
        doNothing().when(fileStore).delete(anyString());

        // when
        memberService.updateMember(1L, name, introduction, profileImage);

        // then
        then(fileStore).should().delete(deleteImage);
    }

    @Test
    @DisplayName("프로필 변경시 프로필 이미지 파라미터가 유효하지 않으면 프로필 이미지는 변경하지 않는다.")
    void updateProfileWithoutProfileImage() {
        // given
        Member member = MemberFixture.memberWithProfileImage();
        String name = "changedName";
        String introduction = "changedIntroduction";
        MockMultipartFile profileImage = null;
        String beforeUpdateProfileImage = member.getProfileImage().getUrl();

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        // when
        memberService.updateMember(1L, name, introduction, profileImage);

        // then
        assertThat(member.getProfileImage().getUrl()).isEqualTo(beforeUpdateProfileImage);
    }

    @Test
    @DisplayName("업데이트할 멤버가 없으면 404 예외가 발생한다")
    void failUpdateMember() {
        // given
        Long memberId = 1L;
        String name = "changedName";
        String introduction = "changedIntroduction";
        MockMultipartFile profileImage = FileFixture.multipartImage("profileImage");
        given(memberRepository.findById(any())).willReturn(Optional.empty());

        // when

        // then
        assertThatThrownBy(() -> memberService.updateMember(memberId, name, introduction, profileImage))
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
