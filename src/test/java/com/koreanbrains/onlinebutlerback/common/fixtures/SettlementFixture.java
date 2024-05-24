package com.koreanbrains.onlinebutlerback.common.fixtures;

import com.koreanbrains.onlinebutlerback.common.page.Page;
import com.koreanbrains.onlinebutlerback.entity.member.Member;
import com.koreanbrains.onlinebutlerback.entity.settlement.SettlementHistory;
import com.koreanbrains.onlinebutlerback.entity.settlement.SettlementStatus;
import com.koreanbrains.onlinebutlerback.repository.settlement.SettlementHistoryDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SettlementFixture {

    public static SettlementHistory settlement(Member member, LocalDateTime createdAt) {
        return SettlementHistory.builder()
                .member(member)
                .amount(10000)
                .status(SettlementStatus.COMPLETE)
                .createdAt(createdAt)
                .build();
    }

    public static Page<SettlementHistoryDto> settlementHistoryDtoPage(int number, int size, int totalElements) {
        List<SettlementHistoryDto> content = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            content.add(new SettlementHistoryDto(i, 1000, SettlementStatus.COMPLETE, LocalDateTime.of(2024 , 5, 1, 0, 0)));
        }
        return new Page<>(content, number, size, totalElements);

    }
}
