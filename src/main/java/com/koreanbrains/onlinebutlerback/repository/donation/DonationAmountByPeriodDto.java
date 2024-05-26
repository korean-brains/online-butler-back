package com.koreanbrains.onlinebutlerback.repository.donation;

import com.koreanbrains.onlinebutlerback.entity.member.Member;

public record DonationAmountByPeriodDto(Member member, long totalAmount) {
}
