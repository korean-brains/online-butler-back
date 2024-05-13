package com.koreanbrains.onlinebutlerback.service.follow;

import com.koreanbrains.onlinebutlerback.common.exception.EntityNotFoundException;
import com.koreanbrains.onlinebutlerback.common.exception.FollowException;
import com.koreanbrains.onlinebutlerback.common.fixtures.FollowFixture;
import com.koreanbrains.onlinebutlerback.common.fixtures.MemberFixture;
import com.koreanbrains.onlinebutlerback.entity.follow.Follow;
import com.koreanbrains.onlinebutlerback.entity.member.Member;
import com.koreanbrains.onlinebutlerback.repository.follow.FollowRepository;
import com.koreanbrains.onlinebutlerback.repository.member.MemberRepository;
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
class FollowServiceTest {

    @InjectMocks
    FollowService followService;

    @Mock
    FollowRepository followRepository;

    @Mock
    MemberRepository memberRepository;

    @Test
    @DisplayName("사용자를 팔로우 한다")
    void follow() {
        // given
        Member follower = MemberFixture.member(1L);
        Member following = MemberFixture.member(2L);
        Follow follow = FollowFixture.follow(follower, following);

        given(followRepository.findByFollowerIdAndFollowingId(anyLong(), anyLong())).willReturn(Optional.empty());
        given(memberRepository.findById(eq(follower.getId()))).willReturn(Optional.of(follower));
        given(memberRepository.findById(eq(following.getId()))).willReturn(Optional.of(following));
        given(followRepository.save(any())).willReturn(follow);

        // when
        followService.follow(follower.getId(), following.getId());

        // then
        then(followRepository).should().save(any());
    }

    @Test
    @DisplayName("이미 팔로우된 사용자를 다시 팔로우 하게 되면 예외가 발생한다")
    void followFailAlready() {
        // given
        Follow follow = FollowFixture.follow();
        given(followRepository.findByFollowerIdAndFollowingId(anyLong(), anyLong())).willReturn(Optional.of(follow));

        // when
        // then
        assertThatThrownBy(() -> followService.follow(1L, 2L))
                .isInstanceOf(FollowException.class);
    }

    @Test
    @DisplayName("팔로우를 요청하는 사용자가 없으면 예외가 발생한다")
    void followFailInvalidFollower() {
        // given
        Member follower = MemberFixture.member(1L);

        given(followRepository.findByFollowerIdAndFollowingId(anyLong(), anyLong())).willReturn(Optional.empty());
        given(memberRepository.findById(eq(follower.getId()))).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> followService.follow(1L, 2L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("팔로우를 요청받는 사용자가 없으면 예외가 발생한다")
    void followFailInvalidFollowing() {
        // given
        Member follower = MemberFixture.member(1L);
        Member following = MemberFixture.member(2L);

        given(followRepository.findByFollowerIdAndFollowingId(anyLong(), anyLong())).willReturn(Optional.empty());
        given(memberRepository.findById(eq(follower.getId()))).willReturn(Optional.of(follower));
        given(memberRepository.findById(eq(following.getId()))).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> followService.follow(1L, 2L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("사용자를 언팔로우 한다")
    void unFollow() {
        // given
        Follow follow = FollowFixture.follow();
        given(followRepository.findByFollowerIdAndFollowingId(anyLong(), anyLong())).willReturn(Optional.of(follow));
        doNothing().when(followRepository).delete(any());

        // when
        followService.unFollow(1L, 2L);

        // then
        then(followRepository).should().delete(follow);
    }

    @Test
    @DisplayName("이미 언팔로우된 사용자를 다시 언팔로우 하게 되면 예외가 발생한다")
    void unFollowFailAlready() {
        // given
        given(followRepository.findByFollowerIdAndFollowingId(anyLong(), anyLong())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> followService.unFollow(1L, 2L))
                .isInstanceOf(FollowException.class);
    }

}