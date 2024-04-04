package com.koreanbrains.onlinebutlerback.repository.donation;

import com.koreanbrains.onlinebutlerback.common.exception.IllegalArgumentException;
import com.koreanbrains.onlinebutlerback.common.fixtures.MemberFixture;
import com.koreanbrains.onlinebutlerback.common.page.Page;
import com.koreanbrains.onlinebutlerback.entity.donation.Donation;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class DonationQueryRepositoryTest {

    @Autowired
    DonationQueryRepository donationQueryRepository;
    @Autowired
    DonationRepository donationRepository;
    @Autowired
    MemberRepository memberRepository;

    Member giver;

    @BeforeEach
    void setup() {
        giver = memberRepository.save(MemberFixture.member(null, "giver 1"));
        List<Member> receivers = memberRepository.saveAll(List.of(
                MemberFixture.member(null, "receiver 1"),
                MemberFixture.member(null, "receiver 2"),
                MemberFixture.member(null, "receiver 3"),
                MemberFixture.member(null, "receiver 4"),
                MemberFixture.member(null, "receiver 5"),
                MemberFixture.member(null, "receiver 6"),
                MemberFixture.member(null, "receiver 7"),
                MemberFixture.member(null, "receiver 8"),
                MemberFixture.member(null, "receiver 9"),
                MemberFixture.member(null, "receiver 10")
        ));

        List<Donation> donations = receivers.stream()
                .map(r -> Donation.builder()
                        .receiver(r)
                        .giver(giver)
                        .amount(1000)
                        .receiptId("66094df500be0400302256f1")
                        .createdAt(LocalDateTime.of(2024, 4, 1, 12, 0))
                        .build())
                .toList();
        donationRepository.saveAll(donations);
    }

    @Test
    @DisplayName("후원 한 내역 페이지네이션 첫 페이지 조회")
    void findGiveHistoryFirstPage() {
        // given
        Long giverId = giver.getId();
        int size = 5;
        int number = 1;
        LocalDateTime start = LocalDateTime.of(2024, 4, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 4, 30, 0, 0);

        // when
        Page<DonationGiveHistoryDto> result = donationQueryRepository.findGiveHistory(giverId, size, number, start, end);

        // then
        assertThat(result.getContent().size()).isEqualTo(5);
        assertThat(result.getSize()).isEqualTo(5);
        assertThat(result.getNumber()).isEqualTo(1);
        assertThat(result.isFirst()).isTrue();
        assertThat(result.isHasNext()).isTrue();
        assertThat(result.getTotalElements()).isEqualTo(10);
        assertThat(result.getTotalPages()).isEqualTo(2);
    }

    @Test
    @DisplayName("후원 한 내역 페이지네이션 마지막 페이지 조회")
    void findGiveHistoryLastPage() {
        // given
        Long giverId = giver.getId();
        int size = 5;
        int number = 2;
        LocalDateTime start = LocalDateTime.of(2024, 4, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 4, 30, 0, 0);

        // when
        Page<DonationGiveHistoryDto> result = donationQueryRepository.findGiveHistory(giverId, size, number, start, end);

        // then
        assertThat(result.getContent().size()).isEqualTo(5);
        assertThat(result.getSize()).isEqualTo(5);
        assertThat(result.getNumber()).isEqualTo(2);
        assertThat(result.isFirst()).isFalse();
        assertThat(result.isHasNext()).isFalse();
        assertThat(result.getTotalElements()).isEqualTo(10);
        assertThat(result.getTotalPages()).isEqualTo(2);
    }

    @Test
    @DisplayName("날짜를 입력하지 않으면 모든 날짜를 조회한다.")
    void findGiveHistoryWithoutDate() {
        // given
        Long giverId = giver.getId();
        int size = 5;
        int number = 1;
        LocalDateTime start = null;
        LocalDateTime end = null;

        // when
        Page<DonationGiveHistoryDto> result = donationQueryRepository.findGiveHistory(giverId, size, number, start, end);

        // then
        assertThat(result.getContent().size()).isEqualTo(5);
        assertThat(result.getSize()).isEqualTo(5);
        assertThat(result.getNumber()).isEqualTo(1);
        assertThat(result.isFirst()).isTrue();
        assertThat(result.isHasNext()).isTrue();
        assertThat(result.getTotalElements()).isEqualTo(10);
        assertThat(result.getTotalPages()).isEqualTo(2);
    }

    @Test
    @DisplayName("날짜를 입력하지 않으면 모든 날짜를 조회한다.")
    void findGiveHistoryWithoutDate2() {
        // given
        Long giverId = giver.getId();
        int size = 5;
        int number = 1;
        LocalDateTime start = LocalDateTime.of(2024, 4, 1, 0, 0);
        LocalDateTime end = null;

        // when
        Page<DonationGiveHistoryDto> result = donationQueryRepository.findGiveHistory(giverId, size, number, start, end);

        // then
        assertThat(result.getContent().size()).isEqualTo(5);
        assertThat(result.getSize()).isEqualTo(5);
        assertThat(result.getNumber()).isEqualTo(1);
        assertThat(result.isFirst()).isTrue();
        assertThat(result.isHasNext()).isTrue();
        assertThat(result.getTotalElements()).isEqualTo(10);
        assertThat(result.getTotalPages()).isEqualTo(2);
    }

    @Test
    @DisplayName("날짜를 입력하지 않으면 모든 날짜를 조회한다.")
    void findGiveHistoryWithoutDate3() {
        // given
        Long giverId = giver.getId();
        int size = 5;
        int number = 1;
        LocalDateTime start = null;
        LocalDateTime end = LocalDateTime.of(2024, 4, 30, 0, 0);

        // when
        Page<DonationGiveHistoryDto> result = donationQueryRepository.findGiveHistory(giverId, size, number, start, end);

        // then
        assertThat(result.getContent().size()).isEqualTo(5);
        assertThat(result.getSize()).isEqualTo(5);
        assertThat(result.getNumber()).isEqualTo(1);
        assertThat(result.isFirst()).isTrue();
        assertThat(result.isHasNext()).isTrue();
        assertThat(result.getTotalElements()).isEqualTo(10);
        assertThat(result.getTotalPages()).isEqualTo(2);
    }

    @Test
    @DisplayName("페이지 번호가 1 보다 작으면 예외가 발생한다.")
    void findGiveHistoryFailPageNumber() {
        // given
        Long giverId = giver.getId();
        int size = 5;
        int number = 0;
        LocalDateTime start = null;
        LocalDateTime end = null;

        // when
        // then
        assertThatThrownBy(() -> donationQueryRepository.findGiveHistory(giverId, size, number, start, end))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("기부자 id가 null이면 예외가 발생한다")
    void findGiveHistoryFailGiverIdNull() {
        // given
        Long giverId = null;
        int size = 5;
        int number = 1;
        LocalDateTime start = null;
        LocalDateTime end = null;

        // when
        // then
        assertThatThrownBy(() -> donationQueryRepository.findGiveHistory(giverId, size, number, start, end))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @TestConfiguration
    static class Config {
        @Bean
        public DonationQueryRepository donationQueryRepository(EntityManager em) {
            return new DonationQueryRepository(em);
        }
    }
}