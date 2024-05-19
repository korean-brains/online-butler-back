package com.koreanbrains.onlinebutlerback.repository.member;

import com.koreanbrains.onlinebutlerback.common.entity.UploadedFile;
import com.koreanbrains.onlinebutlerback.common.fixtures.MemberFixture;
import com.koreanbrains.onlinebutlerback.common.scroll.Scroll;
import com.koreanbrains.onlinebutlerback.entity.member.Member;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class MemberQueryRepositoryTest {

    @Autowired
    MemberQueryRepository memberQueryRepository;

    @Autowired
    MemberRepository memberRepository;

    Member member;

    @BeforeEach
    void setup() {
        member = memberRepository.save(Member.builder()
                .name("kim")
                .email("kim@gmail.com")
                .password("password")
                .profileImage(new UploadedFile("image.jpg", "image.jpg", "assets/image.jpg"))
                .isActive(true)
                .build());
    }

    @Test
    @DisplayName("사용자 정보를 ID로 조회한다")
    void findById() {
        // given
        Long memberId = member.getId();

        // when
        Optional<MemberDto> findById = memberQueryRepository.findById(1L, memberId);

        // then
        assertThat(findById.isPresent()).isTrue();
        assertThat(findById.get().id()).isEqualTo(memberId);
        assertThat(findById.get().name()).isEqualTo(member.getName());
        assertThat(findById.get().email()).isEqualTo(member.getEmail());
        assertThat(findById.get().profileImage()).isEqualTo(member.getProfileImage().getUrl());
        assertThat(findById.get().postCount()).isEqualTo(0);
        assertThat(findById.get().followerCount()).isEqualTo(0);
        assertThat(findById.get().followingCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("조회된 사용자가 없으면 Optional.empty()를 반환한다")
    void findByIdEmpty() {
        // given
        Long memberId = 0L;

        // when
        Optional<MemberDto> findById = memberQueryRepository.findById(1L, memberId);

        // then
        assertThat(findById.isPresent()).isFalse();
    }

    @Test
    @DisplayName("미인증 사용자가 조회하면 언팔로우 상태로 조회된다")
    void findByIdWithoutAuth() {
        // given
        Long memberId = member.getId();

        // when
        Optional<MemberDto> findById = memberQueryRepository.findById(null, memberId);

        // then
        assertThat(findById.isPresent()).isTrue();
        assertThat(findById.get().isFollowed()).isFalse();
    }

    @Test
    @DisplayName("사용자를 무한 스크롤로 검색한다")
    void searchMember() {
        // given
        List<Member> members = memberRepository.saveAll(List.of(MemberFixture.member(null, "member 1"),
                MemberFixture.member(null, "member 2"),
                MemberFixture.member(null, "member 3"),
                MemberFixture.member(null, "member 4"),
                MemberFixture.member(null, "member 5")));

        Long cursor = null;
        int size = 3;
        String name = "member";

        // when
        Scroll<MemberScrollDto> findMembers = memberQueryRepository.scrollSearchMember(cursor, size, name);

        // then
        assertThat(findMembers.getNextCursor()).isEqualTo(members.get(3).getId());
        assertThat(findMembers.getNextSubCursor()).isNull();
        assertThat(findMembers.getContent().size()).isEqualTo(3);
        assertThat(findMembers.getContent().get(0).name()).isEqualTo(members.get(0).getName());
        assertThat(findMembers.getContent().get(1).name()).isEqualTo(members.get(1).getName());
        assertThat(findMembers.getContent().get(2).name()).isEqualTo(members.get(2).getName());
    }

    @Test
    @DisplayName("사용자 검색 결과 무한스크롤의 마지막 페이지를 조회한다")
    void searchMemberLastPage() {
        // given
        List<Member> members = memberRepository.saveAll(List.of(MemberFixture.member(null, "member 1"),
                MemberFixture.member(null, "member 2"),
                MemberFixture.member(null, "member 3"),
                MemberFixture.member(null, "member 4"),
                MemberFixture.member(null, "member 5")));

        Long cursor = members.get(3).getId();
        int size = 3;
        String name = "member";

        // when
        Scroll<MemberScrollDto> findMembers = memberQueryRepository.scrollSearchMember(cursor, size, name);

        // then
        assertThat(findMembers.getNextCursor()).isNull();
        assertThat(findMembers.getNextSubCursor()).isNull();
        assertThat(findMembers.getContent().size()).isEqualTo(2);
        assertThat(findMembers.getContent().get(0).name()).isEqualTo(members.get(3).getName());
        assertThat(findMembers.getContent().get(1).name()).isEqualTo(members.get(4).getName());
    }


    @TestConfiguration
    static class Config {
        @Bean
        public MemberQueryRepository memberQueryRepository(EntityManager entityManager) {
            return new MemberQueryRepository(entityManager);
        }
    }

}