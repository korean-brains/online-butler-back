package com.koreanbrains.onlinebutlerback.repository.settlement;

import com.koreanbrains.onlinebutlerback.entity.settlement.SettlementStatus;

import java.time.LocalDateTime;

public record SettlementHistoryDto(long id,
                                   long amount,
                                   SettlementStatus status,
                                   LocalDateTime createdAt) {
}
