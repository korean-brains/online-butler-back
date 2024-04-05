package com.koreanbrains.onlinebutlerback.repository.donation;

import java.time.LocalDateTime;

public record DonationReceiveHistoryDto(Long id, String giver, int amount, LocalDateTime createdAt) {
}
