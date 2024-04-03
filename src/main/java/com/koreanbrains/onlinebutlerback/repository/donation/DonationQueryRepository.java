package com.koreanbrains.onlinebutlerback.repository.donation;

import com.koreanbrains.onlinebutlerback.common.exception.ErrorCode;
import com.koreanbrains.onlinebutlerback.common.exception.IllegalArgumentException;
import com.koreanbrains.onlinebutlerback.common.page.Page;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.koreanbrains.onlinebutlerback.entity.donation.QDonation.*;

@Repository
public class DonationQueryRepository {

    private final JPAQueryFactory queryFactory;

    public DonationQueryRepository(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Page<DonationGiveHistoryDto> findGiveHistory(Long giverId, int size, int number, LocalDateTime start, LocalDateTime end) {
        validatePageNumber(number);

        List<DonationGiveHistoryDto> result = queryFactory.select(Projections.constructor(DonationGiveHistoryDto.class,
                        donation.id,
                        donation.giver.name,
                        donation.amount,
                        donation.createdAt
                ))
                .from(donation)
                .where(giverIdEq(giverId), betweenDate(start, end))
                .offset((long) size * (number - 1)) // 페이지 번호 1부터 시작
                .limit(size)
                .fetch();

        Long totalCount = queryFactory.select(donation.count())
                .from(donation)
                .where(giverIdEq(giverId), betweenDate(start, end))
                .fetchOne();

        return new Page<>(result, number, size, totalCount == null ? 0 : totalCount);
    }

    private BooleanExpression giverIdEq(Long id) {
        return id == null ? null : donation.giver.id.eq(id);
    }

    private BooleanExpression betweenDate(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null){
            return null;
        }
        return donation.createdAt.between(start, end);
    }

    private void validatePageNumber(int number) {
        if(number < 1) {
            throw new IllegalArgumentException(ErrorCode.DONATION_HISTORY_INVALID_PAGE_NUMBER);
        }
    }
}
