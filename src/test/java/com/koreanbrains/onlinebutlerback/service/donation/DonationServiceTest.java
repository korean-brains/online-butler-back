package com.koreanbrains.onlinebutlerback.service.donation;

import com.koreanbrains.onlinebutlerback.common.exception.DonationException;
import com.koreanbrains.onlinebutlerback.common.exception.EntityNotFoundException;
import com.koreanbrains.onlinebutlerback.common.fixtures.DonationFixture;
import com.koreanbrains.onlinebutlerback.common.fixtures.MemberFixture;
import com.koreanbrains.onlinebutlerback.common.util.bootpay.BootpayClient;
import com.koreanbrains.onlinebutlerback.entity.member.Member;
import com.koreanbrains.onlinebutlerback.repository.donation.DonationRepository;
import com.koreanbrains.onlinebutlerback.repository.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class DonationServiceTest {

    @InjectMocks
    DonationService donationService;
    @Mock
    BootpayClient bootpayClient;
    @Mock
    MemberRepository memberRepository;
    @Mock
    DonationRepository donationRepository;


    @Test
    @DisplayName("부트페이로부터 결제 내역을 가져와 DB에 저장한다.")
    void save() {
        // given
        String receiptId = "66094df500be0400302256f1";
        Member giver = MemberFixture.member(1L);
        Member receiver = MemberFixture.member(2L);
        HashMap<String, Object> receipt = DonationFixture.receipt();
        String message = "message";

        given(memberRepository.findById(eq(1L))).willReturn(Optional.of(giver));
        given(memberRepository.findById(eq(2L))).willReturn(Optional.of(receiver));
        given(bootpayClient.getReceipt(anyString())).willReturn(receipt);

        // when
        donationService.save(receiptId, giver.getId(), receiver.getId(), message);

        // then
        then(donationRepository).should().save(any());
    }

    @Test
    @DisplayName("후원 한 사용자가 존재하지 않으면 예외가 발생한다.")
    void saveFailNoGiver() {
        // given
        String receiptId = "66094df500be0400302256f1";
        Member giver = MemberFixture.member(1L);
        Member receiver = MemberFixture.member(2L);
        String message = "message";

        given(memberRepository.findById(eq(1L))).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> donationService.save(receiptId, giver.getId(), receiver.getId(), message))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("후원 받는 사용자가 존재하지 않으면 예외가 발생한다.")
    void saveFailNoReceiver() {
        // given
        String receiptId = "66094df500be0400302256f1";
        Member giver = MemberFixture.member(1L);
        Member receiver = MemberFixture.member(2L);
        String message = "message";

        given(memberRepository.findById(eq(1L))).willReturn(Optional.of(giver));
        given(memberRepository.findById(eq(2L))).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> donationService.save(receiptId, giver.getId(), receiver.getId(), message))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("결제 내역 조회 도중 문제가 생기면 예외가 발생한다.")
    void saveFailGetReceipt() {
        // given
        String receiptId = "66094df500be0400302256f1";
        Member giver = MemberFixture.member(1L);
        Member receiver = MemberFixture.member(2L);
        HashMap<String, Object> receipt = DonationFixture.receiptWithErrorCode();
        String message = "message";

        given(memberRepository.findById(eq(1L))).willReturn(Optional.of(giver));
        given(memberRepository.findById(eq(2L))).willReturn(Optional.of(receiver));
        given(bootpayClient.getReceipt(anyString())).willReturn(receipt);

        // when
        // then
        assertThatThrownBy(() -> donationService.save(receiptId, giver.getId(), receiver.getId(), message))
                .isInstanceOf(DonationException.class);
    }


}