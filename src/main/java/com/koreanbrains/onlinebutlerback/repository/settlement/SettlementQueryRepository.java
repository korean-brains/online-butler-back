package com.koreanbrains.onlinebutlerback.repository.settlement;

import com.koreanbrains.onlinebutlerback.common.page.Page;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.koreanbrains.onlinebutlerback.entity.donation.QDonation.donation;
import static com.koreanbrains.onlinebutlerback.entity.settlement.QSettlementHistory.settlementHistory;

@Repository
public class SettlementQueryRepository {

    private final JPAQueryFactory queryFactory;

    public SettlementQueryRepository(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public int getDonationAmountByPeriod(long memberId, LocalDateTime start, LocalDateTime end) {
        return queryFactory.select(donation.amount.sum())
                .from(donation)
                .where(donation.receiver.id.eq(memberId)
                        .and(donation.createdAt.between(start, end)))
                .fetchOne()
                .intValue();
    }

    public Page<SettlementHistoryDto> getSettlementHistoryPageByPeriod(long memberId, LocalDateTime start, LocalDateTime end, int number, int size) {
        List<SettlementHistoryDto> result = queryFactory.select(Projections.constructor(SettlementHistoryDto.class,
                        settlementHistory.id,
                        settlementHistory.amount,
                        settlementHistory.status,
                        settlementHistory.createdAt
                ))
                .from(settlementHistory)
                .where(settlementHistory.member.id.eq(memberId),
                        createdAtBetween(start, end))
                .orderBy(settlementHistory.createdAt.desc())
                .offset((long) size * (number - 1)) // 페이지 번호 1부터 시작
                .limit(size)
                .fetch();

        long count = queryFactory.select(settlementHistory.count())
                .from(settlementHistory)
                .where(settlementHistory.member.id.eq(memberId),
                        createdAtBetween(start, end))
                .fetchOne()
                .longValue();

        return new Page<>(result, number, size, count);
    }

    private BooleanExpression createdAtBetween(LocalDateTime start, LocalDateTime end) {
        return start == null || end == null ? null : settlementHistory.createdAt.between(start, end);
    }
}
