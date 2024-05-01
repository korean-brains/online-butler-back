package com.koreanbrains.onlinebutlerback.service.donation;

import com.koreanbrains.onlinebutlerback.common.exception.DonationException;
import com.koreanbrains.onlinebutlerback.common.exception.EntityNotFoundException;
import com.koreanbrains.onlinebutlerback.common.exception.ErrorCode;
import com.koreanbrains.onlinebutlerback.common.util.bootpay.BootpayClient;
import com.koreanbrains.onlinebutlerback.entity.donation.Donation;
import com.koreanbrains.onlinebutlerback.entity.member.Member;
import com.koreanbrains.onlinebutlerback.repository.donation.DonationRepository;
import com.koreanbrains.onlinebutlerback.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DonationService {

    private final DonationRepository donationRepository;
    private final BootpayClient bootpayClient;
    private final MemberRepository memberRepository;

    @Transactional
    public void save(String receiptId, Long giverId, Long receiverId) {
        Member giver = memberRepository.findById(giverId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        Member receiver = memberRepository.findById(receiverId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        HashMap<String, Object> receipt = bootpayClient.getReceipt(receiptId);

        if(receipt.get("error_code") != null) {
            throw new DonationException(ErrorCode.DONATION_BOOTPAY_CONFIRM_FAIL);
        }

        int amount = (int) receipt.get("price");

        LocalDateTime createdAt = LocalDateTime.parse((String) receipt.get("purchased_at"), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXXXX"));

        Donation donation = Donation.builder()
                .receiptId(receiptId)
                .giver(giver)
                .receiver(receiver)
                .amount(amount)
                .createdAt(createdAt)
                .build();

        donationRepository.save(donation);
    }
}
