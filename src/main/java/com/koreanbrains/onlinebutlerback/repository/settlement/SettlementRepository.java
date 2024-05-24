package com.koreanbrains.onlinebutlerback.repository.settlement;

import com.koreanbrains.onlinebutlerback.entity.settlement.SettlementHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementRepository extends JpaRepository<SettlementHistory, Long> {
}
