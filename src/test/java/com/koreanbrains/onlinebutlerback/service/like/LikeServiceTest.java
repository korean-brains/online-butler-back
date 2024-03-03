package com.koreanbrains.onlinebutlerback.service.like;

import com.koreanbrains.onlinebutlerback.common.exception.EntityExistsException;
import com.koreanbrains.onlinebutlerback.common.exception.EntityNotFoundException;
import com.koreanbrains.onlinebutlerback.common.fixtures.LikeFixture;
import com.koreanbrains.onlinebutlerback.common.fixtures.MemberFixture;
import com.koreanbrains.onlinebutlerback.common.fixtures.PostFixture;
import com.koreanbrains.onlinebutlerback.entity.like.Like;
import com.koreanbrains.onlinebutlerback.entity.member.Member;
import com.koreanbrains.onlinebutlerback.entity.post.Post;
import com.koreanbrains.onlinebutlerback.repository.like.LikeRepository;
import com.koreanbrains.onlinebutlerback.repository.member.MemberRepository;
import com.koreanbrains.onlinebutlerback.repository.post.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    @InjectMocks
    LikeService likeService;

    @Mock
    PostRepository postRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    LikeRepository likeRepository;

    @Test
    @DisplayName("포스트에 좋아요를 누른다")
    void likePost() {
        // given
        Post post = PostFixture.post();
        Member member = MemberFixture.member();

        given(likeRepository.findByPostIdAndMemberId(anyLong(), anyLong())).willReturn(Optional.empty());
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        // when
        likeService.likePost(post.getId(), member.getId());

        // then
        then(likeRepository).should().save(any());
    }

    @Test
    @DisplayName("이미 좋아요를 누른 포스트는 다시 좋아요를 누를 수 없다")
    void likePostDuplicate() {
        // given
        Like like = LikeFixture.like();
        Post post = PostFixture.post();
        Member member = MemberFixture.member();
        given(likeRepository.findByPostIdAndMemberId(anyLong(), anyLong())).willReturn(Optional.of(like));

        // when
        // then
        assertThatThrownBy(() -> likeService.likePost(post.getId(), member.getId()))
                .isInstanceOf(EntityExistsException.class);
    }

    @Test
    @DisplayName("존재하는 포스트에만 좋아요를 누를 수 있다")
    void likePostExistsPost() {
        // given
        Post post = PostFixture.post();
        Member member = MemberFixture.member();

        given(likeRepository.findByPostIdAndMemberId(anyLong(), anyLong())).willReturn(Optional.empty());
        given(postRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> likeService.likePost(post.getId(), member.getId()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("서비스에 가입한 회원만 좋아요를 누를 수 있다")
    void likePostExistsMember() {
        // given
        Post post = PostFixture.post();
        Member member = MemberFixture.member();

        given(likeRepository.findByPostIdAndMemberId(anyLong(), anyLong())).willReturn(Optional.empty());
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> likeService.likePost(post.getId(), member.getId()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("포스트 좋아요를 취소한다")
    void dislikePost() {
        // given
        Like like = LikeFixture.like();
        Post post = PostFixture.post();
        Member member = MemberFixture.member();
        given(likeRepository.findByPostIdAndMemberId(anyLong(), anyLong())).willReturn(Optional.of(like));

        // when
        likeService.dislikePost(post.getId(), member.getId());

        // then
        then(likeRepository).should().delete(like);
    }

    @Test
    @DisplayName("좋아요를 누른 포스트만 취소할 수 있다")
    void dislikePostAlreadyLike() {
        // given
        Post post = PostFixture.post();
        Member member = MemberFixture.member();
        given(likeRepository.findByPostIdAndMemberId(anyLong(), anyLong())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> likeService.dislikePost(post.getId(), member.getId()))
                .isInstanceOf(EntityNotFoundException.class);
    }

}