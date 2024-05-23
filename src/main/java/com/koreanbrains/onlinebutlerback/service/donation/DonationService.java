package com.koreanbrains.onlinebutlerback.service.donation;

import com.koreanbrains.onlinebutlerback.common.exception.EntityNotFoundException;
import com.koreanbrains.onlinebutlerback.common.exception.ErrorCode;
import com.koreanbrains.onlinebutlerback.entity.donation.Donation;
import com.koreanbrains.onlinebutlerback.entity.member.Member;
import com.koreanbrains.onlinebutlerback.repository.donation.DonationRepository;
import com.koreanbrains.onlinebutlerback.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DonationService {

    private final DonationRepository donationRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void save(String paymentId, int amount, Long giverId, Long receiverId, String message) {
        Member giver = memberRepository.findById(giverId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        Member receiver = memberRepository.findById(receiverId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        Donation donation = Donation.builder()
                .paymentId(paymentId)
                .giver(giver)
                .receiver(receiver)
                .amount(amount)
                .message(message)
                .createdAt(LocalDateTime.now())
                .build();

        donationRepository.save(donation);
    }
}
