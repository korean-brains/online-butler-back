package com.koreanbrains.onlinebutlerback.service.donation;

import com.koreanbrains.onlinebutlerback.common.exception.EntityNotFoundException;
import com.koreanbrains.onlinebutlerback.common.fixtures.MemberFixture;
import com.koreanbrains.onlinebutlerback.entity.member.Member;
import com.koreanbrains.onlinebutlerback.repository.donation.DonationRepository;
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
class DonationServiceTest {

    @InjectMocks
    DonationService donationService;
    @Mock
    MemberRepository memberRepository;
    @Mock
    DonationRepository donationRepository;


    @Test
    @DisplayName("후원 결과를 DB에 저장한다.")
    void save() {
        // given
        String paymentId = "payment-febc83d3-9229-4fcd-b8be-50c575b5fafa";
        int amount = 1000;
        Member giver = MemberFixture.member(1L);
        Member receiver = MemberFixture.member(2L);
        String message = "message";

        given(memberRepository.findById(eq(1L))).willReturn(Optional.of(giver));
        given(memberRepository.findById(eq(2L))).willReturn(Optional.of(receiver));

        // when
        donationService.save(paymentId, amount, giver.getId(), receiver.getId(), message);

        // then
        then(donationRepository).should().save(any());
    }

    @Test
    @DisplayName("후원 한 사용자가 존재하지 않으면 예외가 발생한다.")
    void saveFailNoGiver() {
        // given
        String paymentId = "payment-febc83d3-9229-4fcd-b8be-50c575b5fafa";
        int amount = 1000;
        Member giver = MemberFixture.member(1L);
        Member receiver = MemberFixture.member(2L);
        String message = "message";

        given(memberRepository.findById(eq(1L))).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> donationService.save(paymentId, amount, giver.getId(), receiver.getId(), message))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("후원 받는 사용자가 존재하지 않으면 예외가 발생한다.")
    void saveFailNoReceiver() {
        // given
        String paymentId = "payment-febc83d3-9229-4fcd-b8be-50c575b5fafa";
        int amount = 1000;
        Member giver = MemberFixture.member(1L);
        Member receiver = MemberFixture.member(2L);
        String message = "message";

        given(memberRepository.findById(eq(1L))).willReturn(Optional.of(giver));
        given(memberRepository.findById(eq(2L))).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> donationService.save(paymentId, amount, giver.getId(), receiver.getId(), message))
                .isInstanceOf(EntityNotFoundException.class);
    }


}