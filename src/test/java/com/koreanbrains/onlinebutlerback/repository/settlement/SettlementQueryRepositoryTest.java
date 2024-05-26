package com.koreanbrains.onlinebutlerback.repository.settlement;

import com.koreanbrains.onlinebutlerback.common.fixtures.DonationFixture;
import com.koreanbrains.onlinebutlerback.common.fixtures.MemberFixture;
import com.koreanbrains.onlinebutlerback.common.fixtures.SettlementFixture;
import com.koreanbrains.onlinebutlerback.common.page.Page;
import com.koreanbrains.onlinebutlerback.entity.member.Member;
import com.koreanbrains.onlinebutlerback.repository.donation.DonationRepository;
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

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SettlementQueryRepositoryTest {

    @Autowired SettlementQueryRepository settlementQueryRepository;
    @Autowired SettlementRepository settlementRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    DonationRepository donationRepository;

    Member member;

    @BeforeEach
    void setup() {
        member = memberRepository.save(MemberFixture.member(null, "kim"));
        settlementRepository.saveAll(List.of(
                SettlementFixture.settlement(member, LocalDateTime.of(2023, 12, 1, 0, 0)),
                SettlementFixture.settlement(member, LocalDateTime.of(2023, 11, 1, 0, 0)),
                SettlementFixture.settlement(member, LocalDateTime.of(2023, 10, 1, 0, 0)),
                SettlementFixture.settlement(member, LocalDateTime.of(2023, 9, 1, 0, 0)),
                SettlementFixture.settlement(member, LocalDateTime.of(2023, 8, 1, 0, 0)),
                SettlementFixture.settlement(member, LocalDateTime.of(2023, 7, 1, 0, 0)),
                SettlementFixture.settlement(member, LocalDateTime.of(2023, 6, 1, 0, 0)),
                SettlementFixture.settlement(member, LocalDateTime.of(2023, 5, 1, 0, 0)),
                SettlementFixture.settlement(member, LocalDateTime.of(2023, 4, 1, 0, 0)),
                SettlementFixture.settlement(member, LocalDateTime.of(2023, 3, 1, 0, 0))
        ));

        donationRepository.saveAll(List.of(
            DonationFixture.donation(member, member, 1000, LocalDateTime.of(2023, 9, 1, 0, 0)),
            DonationFixture.donation(member, member, 1000, LocalDateTime.of(2023, 9, 2, 0, 0)),
            DonationFixture.donation(member, member, 1000, LocalDateTime.of(2023, 9, 3, 0, 0)),
            DonationFixture.donation(member, member, 1000, LocalDateTime.of(2023, 9, 4, 0, 0)),
            DonationFixture.donation(member, member, 1000, LocalDateTime.of(2023, 9, 5, 0, 0)),
            DonationFixture.donation(member, member, 1000, LocalDateTime.of(2023, 8, 1, 0, 0)),
            DonationFixture.donation(member, member, 1000, LocalDateTime.of(2023, 8, 1, 0, 0)),
            DonationFixture.donation(member, member, 1000, LocalDateTime.of(2023, 8, 1, 0, 0)),
            DonationFixture.donation(member, member, 1000, LocalDateTime.of(2023, 8, 1, 0, 0)),
            DonationFixture.donation(member, member, 1000, LocalDateTime.of(2023, 8, 1, 0, 0))
        ));
    }

    @Test
    @DisplayName("특정 기간의 정산 지급금 내역을 조회 한다")
    void getSettlementHistoryPageByPeriod() {
        // given
        long memberId = member.getId();
        LocalDateTime start = LocalDateTime.of(2023, 5, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 10, 1, 0, 0);
        int number = 1;
        int size = 3;

        // when
        Page<SettlementHistoryDto> result = settlementQueryRepository.getSettlementHistoryPageByPeriod(memberId, start, end, number, size);

        // then
        assertThat(result.isFirst()).isTrue();
        assertThat(result.getNumber()).isEqualTo(1);
        assertThat(result.getSize()).isEqualTo(3);
        assertThat(result.getTotalPages()).isEqualTo(2);
        assertThat(result.getTotalElements()).isEqualTo(6);
        assertThat(result.getContent().get(0).createdAt()).isEqualTo(LocalDateTime.of(2023, 10, 1, 0, 0));
        assertThat(result.getContent().get(2).createdAt()).isEqualTo(LocalDateTime.of(2023, 8, 1, 0, 0));
    }

    @Test
    @DisplayName("기간을 정하지 않으면 모든 정산 지급금 내역을 조회 한다")
    void getSettlementHistoryPageWithoutPeriod() {
        // given
        long memberId = member.getId();
        LocalDateTime start = LocalDateTime.of(2023, 5, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 10, 1, 0, 0);
        int number = 1;
        int size = 3;

        // when
        Page<SettlementHistoryDto> result = settlementQueryRepository.getSettlementHistoryPageByPeriod(memberId, start, null, number, size);

        // then
        assertThat(result.isFirst()).isTrue();
        assertThat(result.getNumber()).isEqualTo(1);
        assertThat(result.getSize()).isEqualTo(3);
        assertThat(result.getTotalPages()).isEqualTo(4);
        assertThat(result.getTotalElements()).isEqualTo(10);
        assertThat(result.getContent().get(0).createdAt()).isEqualTo(LocalDateTime.of(2023, 12, 1, 0, 0));
        assertThat(result.getContent().get(2).createdAt()).isEqualTo(LocalDateTime.of(2023, 10, 1, 0, 0));


        // when
        result = settlementQueryRepository.getSettlementHistoryPageByPeriod(memberId, null, end, number, size);

        // then
        assertThat(result.isFirst()).isTrue();
        assertThat(result.getNumber()).isEqualTo(1);
        assertThat(result.getSize()).isEqualTo(3);
        assertThat(result.getTotalPages()).isEqualTo(4);
        assertThat(result.getTotalElements()).isEqualTo(10);
        assertThat(result.getContent().get(0).createdAt()).isEqualTo(LocalDateTime.of(2023, 12, 1, 0, 0));
        assertThat(result.getContent().get(2).createdAt()).isEqualTo(LocalDateTime.of(2023, 10, 1, 0, 0));

        // when
        result = settlementQueryRepository.getSettlementHistoryPageByPeriod(memberId, null, null, number, size);

        // then
        assertThat(result.isFirst()).isTrue();
        assertThat(result.getNumber()).isEqualTo(1);
        assertThat(result.getSize()).isEqualTo(3);
        assertThat(result.getTotalPages()).isEqualTo(4);
        assertThat(result.getTotalElements()).isEqualTo(10);
        assertThat(result.getContent().get(0).createdAt()).isEqualTo(LocalDateTime.of(2023, 12, 1, 0, 0));
        assertThat(result.getContent().get(2).createdAt()).isEqualTo(LocalDateTime.of(2023, 10, 1, 0, 0));
    }

    @Test
    @DisplayName("이번 달 후원 금액을 조회 한다")
    void getDonationAmountByPeriod() {
        // given
        long memberId = member.getId();
        LocalDateTime start = LocalDateTime.of(2023, 9, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 10, 1, 0, 0);

        // when
        int totalAmount = settlementQueryRepository.getDonationAmountByPeriod(memberId, start, end);

        // then
        assertThat(totalAmount).isEqualTo(5000);
    }


    @TestConfiguration
    static class Config {
        @Bean
        public SettlementQueryRepository settlementQueryRepository(EntityManager entityManager) {
            return new SettlementQueryRepository(entityManager);
        }
    }

}