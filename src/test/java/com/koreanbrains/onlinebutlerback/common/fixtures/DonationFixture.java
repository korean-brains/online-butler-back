package com.koreanbrains.onlinebutlerback.common.fixtures;

import com.koreanbrains.onlinebutlerback.entity.donation.Donation;
import com.koreanbrains.onlinebutlerback.entity.member.Member;

import java.time.LocalDateTime;

public class DonationFixture {

    public static Donation donation(Member giver, Member receiver, int amount, LocalDateTime createdAt) {
        return Donation.builder()
                .paymentId("payment-febc83d3-9229-4fcd-b8be-50c575b5fafa")
                .message("text")
                .receiver(receiver)
                .giver(giver)
                .amount(amount)
                .createdAt(createdAt)
                .build();
    }
}
