package com.koreanbrains.onlinebutlerback.service.settlement;

import com.koreanbrains.onlinebutlerback.entity.settlement.SettlementHistory;
import com.koreanbrains.onlinebutlerback.entity.settlement.SettlementStatus;
import com.koreanbrains.onlinebutlerback.repository.donation.DonationAmountByPeriodDto;
import com.koreanbrains.onlinebutlerback.repository.donation.DonationRepository;
import com.koreanbrains.onlinebutlerback.repository.settlement.SettlementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SettlementService {

    private final SettlementRepository settlementRepository;
    private final DonationRepository donationRepository;

    @Scheduled(cron = "0 0 0 1 * *")
    @Transactional
    public void settlement() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0);
        LocalDateTime start = end.minusMonths(1);
        List<DonationAmountByPeriodDto> aggregation = donationRepository.findAggregationTotalAmountByPeriod(start, end);

        aggregation.forEach(a -> {
            SettlementHistory settlementHistory = SettlementHistory.builder()
                    .member(a.member())
                    .amount(a.totalAmount())
                    .status(SettlementStatus.COMPLETE)
                    .build();

            settlementRepository.save(settlementHistory);
        });
    }

}
