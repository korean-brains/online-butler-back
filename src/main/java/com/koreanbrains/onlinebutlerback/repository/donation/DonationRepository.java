package com.koreanbrains.onlinebutlerback.repository.donation;

import com.koreanbrains.onlinebutlerback.entity.donation.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface DonationRepository extends JpaRepository<Donation, Long> {

    @Query("select new com.koreanbrains.onlinebutlerback.repository.donation.DonationAmountByPeriodDto(" +
            "  d.receiver, " +
            "  sum(d.amount) " +
            ")" +
            "from Donation d " +
            "where " +
            "  d.createdAt >= :start " +
            "  and d.createdAt < :end " +
            "group by d.receiver")
    List<DonationAmountByPeriodDto> findAggregationTotalAmountByPeriod(LocalDateTime start, LocalDateTime end);
}
