package com.koreanbrains.onlinebutlerback.repository.follow;

import com.koreanbrains.onlinebutlerback.common.fixtures.FollowFixture;
import com.koreanbrains.onlinebutlerback.common.fixtures.MemberFixture;
import com.koreanbrains.onlinebutlerback.common.scroll.Scroll;
import com.koreanbrains.onlinebutlerback.entity.follow.Follow;
import com.koreanbrains.onlinebutlerback.entity.member.Member;
import com.koreanbrains.onlinebutlerback.repository.member.MemberRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FollowQueryRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    FollowRepository followRepository;
    @Autowired
            FollowQueryRepository followQueryRepository;

    Member member;
    List<Follow> followers = new ArrayList<>();
    List<Follow> followings = new ArrayList<>();

    @BeforeEach
    void setup() {
         member = memberRepository.save(MemberFixture.member());
        List<Member> members = memberRepository.saveAll(List.of(
                MemberFixture.member(null, "member1"),
                MemberFixture.member(null, "member2"),
                MemberFixture.member(null, "member3"),
                MemberFixture.member(null, "member4"),
                MemberFixture.member(null, "member5"),
                MemberFixture.member(null, "member6"),
                MemberFixture.member(null, "member7"),
                MemberFixture.member(null, "member8"),
                MemberFixture.member(null, "member9"),
                MemberFixture.member(null, "member10")
        ));


        followers.clear();
        followings.clear();
        members.forEach((m) -> {
            followers.add(followRepository.save(FollowFixture.follow(null, m, member)));
            followings.add(followRepository.save(FollowFixture.follow(null, member, m)));
        });
    }

    @Test
    @DisplayName("팔로우 목록을 무한스크롤로 조회한다")
    void findFollowingList() {
        // given
        int size = 5;
        Long memberId = member.getId();
        Long cursor = null;

        // when
        Scroll<FollowDto> followingList = followQueryRepository.findFollowingList(memberId, cursor, size);

        // then
        assertThat(followingList.getContent().size()).isEqualTo(5);
        assertThat(followingList.getNextCursor()).isEqualTo(followings.get(size).getId());
        assertThat(followingList.getNextSubCursor()).isNull();
        assertThat(followingList.getContent().get(0).name()).isEqualTo(followings.get(0).getFollowing().getName());
        assertThat(followingList.getContent().get(1).name()).isEqualTo(followings.get(1).getFollowing().getName());
        assertThat(followingList.getContent().get(2).name()).isEqualTo(followings.get(2).getFollowing().getName());
        assertThat(followingList.getContent().get(3).name()).isEqualTo(followings.get(3).getFollowing().getName());
        assertThat(followingList.getContent().get(4).name()).isEqualTo(followings.get(4).getFollowing().getName());
    }

    @Test
    @DisplayName("팔로우 목록을 무한스크롤의 마지막 페이지를 조회한다")
    void findFollowingListLastPage() {
        // given
        int size = 5;
        Long memberId = member.getId();
        Long cursor = followings.get(size).getId();

        // when
        Scroll<FollowDto> followingList = followQueryRepository.findFollowingList(memberId, cursor, size);

        // then
        assertThat(followingList.getContent().size()).isEqualTo(5);
        assertThat(followingList.getNextCursor()).isNull();
        assertThat(followingList.getNextSubCursor()).isNull();
        assertThat(followingList.getContent().get(0).name()).isEqualTo(followings.get(5).getFollowing().getName());
        assertThat(followingList.getContent().get(1).name()).isEqualTo(followings.get(6).getFollowing().getName());
        assertThat(followingList.getContent().get(2).name()).isEqualTo(followings.get(7).getFollowing().getName());
        assertThat(followingList.getContent().get(3).name()).isEqualTo(followings.get(8).getFollowing().getName());
        assertThat(followingList.getContent().get(4).name()).isEqualTo(followings.get(9).getFollowing().getName());
    }

    @Test
    @DisplayName("팔로워 목록을 무한스크롤로 조회한다")
    void findFollowerList() {
        // given
        int size = 5;
        Long memberId = member.getId();
        Long cursor = null;

        // when
        Scroll<FollowDto> followingList = followQueryRepository.findFollowerList(memberId, cursor, size);

        // then
        assertThat(followingList.getContent().size()).isEqualTo(5);
        assertThat(followingList.getNextCursor()).isEqualTo(followers.get(size).getId());
        assertThat(followingList.getNextSubCursor()).isNull();
        assertThat(followingList.getContent().get(0).name()).isEqualTo(followers.get(0).getFollower().getName());
        assertThat(followingList.getContent().get(1).name()).isEqualTo(followers.get(1).getFollower().getName());
        assertThat(followingList.getContent().get(2).name()).isEqualTo(followers.get(2).getFollower().getName());
        assertThat(followingList.getContent().get(3).name()).isEqualTo(followers.get(3).getFollower().getName());
        assertThat(followingList.getContent().get(4).name()).isEqualTo(followers.get(4).getFollower().getName());
    }

    @Test
    @DisplayName("팔로워 목록을 무한스크롤의 마지막 페이지를 조회한다")
    void findFollowerListLastPage() {
        // given
        int size = 5;
        Long memberId = member.getId();
        Long cursor = followers.get(size).getId();

        // when
        Scroll<FollowDto> followingList = followQueryRepository.findFollowerList(memberId, cursor, size);

        // then
        assertThat(followingList.getContent().size()).isEqualTo(5);
        assertThat(followingList.getNextCursor()).isNull();
        assertThat(followingList.getNextSubCursor()).isNull();
        assertThat(followingList.getContent().get(0).name()).isEqualTo(followers.get(5).getFollower().getName());
        assertThat(followingList.getContent().get(1).name()).isEqualTo(followers.get(6).getFollower().getName());
        assertThat(followingList.getContent().get(2).name()).isEqualTo(followers.get(7).getFollower().getName());
        assertThat(followingList.getContent().get(3).name()).isEqualTo(followers.get(8).getFollower().getName());
        assertThat(followingList.getContent().get(4).name()).isEqualTo(followers.get(9).getFollower().getName());
    }


    @TestConfiguration
    public static class Config {
        @Bean
        FollowQueryRepository followQueryRepository(EntityManager entityManager) {
            return new FollowQueryRepository(entityManager);
        }
    }

}