package com.koreanbrains.onlinebutlerback.repository.donation;

import java.time.LocalDateTime;

public record DonationGiveHistoryDto(Long id, String receiver, int amount, LocalDateTime createdAt) {
}
