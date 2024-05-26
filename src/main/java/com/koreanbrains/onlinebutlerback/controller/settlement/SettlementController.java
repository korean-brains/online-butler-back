package com.koreanbrains.onlinebutlerback.controller.settlement;

import com.koreanbrains.onlinebutlerback.common.page.Page;
import com.koreanbrains.onlinebutlerback.common.security.annotation.AuthUser;
import com.koreanbrains.onlinebutlerback.common.security.dto.AccountDto;
import com.koreanbrains.onlinebutlerback.repository.settlement.SettlementHistoryDto;
import com.koreanbrains.onlinebutlerback.repository.settlement.SettlementQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/settlement")
public class SettlementController {

    private final SettlementQueryRepository settlementQueryRepository;

    @GetMapping("/monthly")
    @PreAuthorize("isAuthenticated()")
    public MonthlyDonationAmountResponse getMonthlyDonationAmount(@AuthUser AccountDto accountDto) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0);
        int totalAmount = settlementQueryRepository.getDonationAmountByPeriod(accountDto.getId(), start, now);

        return new MonthlyDonationAmountResponse(totalAmount);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public Page<SettlementHistoryDto> getSettlementHistory(@AuthUser AccountDto accountDto,
                                                           @ModelAttribute SettlementHistoryGetRequest request) {

        return settlementQueryRepository.getSettlementHistoryPageByPeriod(accountDto.getId(), request.getStart(), request.getEnd(), request.getNumber(), request.getSize());
    }
}
