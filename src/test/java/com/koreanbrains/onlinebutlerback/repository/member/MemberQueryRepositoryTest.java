package com.koreanbrains.onlinebutlerback.repository.member;

import com.koreanbrains.onlinebutlerback.common.entity.UploadedFile;
import com.koreanbrains.onlinebutlerback.entity.member.Member;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

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
        Optional<MemberDto> findById = memberQueryRepository.findById(memberId);

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
        Optional<MemberDto> findById = memberQueryRepository.findById(memberId);

        // then
        assertThat(findById.isPresent()).isFalse();
    }


    @TestConfiguration
    static class Config {
        @Bean
        public MemberQueryRepository memberQueryRepository(EntityManager entityManager) {
            return new MemberQueryRepository(entityManager);
        }
    }

}