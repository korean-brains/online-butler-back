package com.koreanbrains.onlinebutlerback.repository.donation;

import com.koreanbrains.onlinebutlerback.common.exception.ErrorCode;
import com.koreanbrains.onlinebutlerback.common.exception.IllegalArgumentException;
import com.koreanbrains.onlinebutlerback.common.page.Page;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.koreanbrains.onlinebutlerback.entity.donation.QDonation.*;
import static com.koreanbrains.onlinebutlerback.entity.member.QMember.member;

@Repository
public class DonationQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final JdbcTemplate jdbcTemplate;

    public DonationQueryRepository(EntityManager entityManager, JdbcTemplate jdbcTemplate) {
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.jdbcTemplate = jdbcTemplate;
    }

    public Page<DonationGiveHistoryDto> findGiveHistory(Long giverId, int size, int number, LocalDateTime start, LocalDateTime end) {
        validatePageNumber(number);

        List<DonationGiveHistoryDto> result = queryFactory.select(Projections.constructor(DonationGiveHistoryDto.class,
                        donation.id,
                        donation.receiver.name,
                        donation.amount,
                        donation.createdAt
                ))
                .from(donation)
                .join(donation.receiver, member)
                .where(giverIdEq(giverId), betweenDate(start, end))
                .orderBy(donation.createdAt.desc())
                .offset((long) size * (number - 1)) // 페이지 번호 1부터 시작
                .limit(size)
                .fetch();

        long totalCount = queryFactory.select(donation.count())
                .from(donation)
                .where(giverIdEq(giverId), betweenDate(start, end))
                .fetchOne()
                .longValue();

        return new Page<>(result, number, size, totalCount);
    }

    public Page<DonationReceiveHistoryDto> findReceiveHistory(Long receiverId, int size, int number, LocalDateTime start, LocalDateTime end) {
        validatePageNumber(number);

        List<DonationReceiveHistoryDto> result = queryFactory.select(Projections.constructor(DonationReceiveHistoryDto.class,
                        donation.id,
                        donation.giver.name,
                        donation.amount,
                        donation.createdAt
                ))
                .from(donation)
                .join(donation.giver, member)
                .where(receiverId(receiverId), betweenDate(start, end))
                .orderBy(donation.createdAt.desc())
                .offset((long) size * (number - 1)) // 페이지 번호 1부터 시작
                .limit(size)
                .fetch();

        long totalCount = queryFactory.select(donation.count())
                .from(donation)
                .where(receiverId(receiverId), betweenDate(start, end))
                .fetchOne()
                .longValue();

        return new Page<>(result, number, size, totalCount);
    }

    public Page<DonationReceiveRankingDto> findReceiveRanking(long receiverId, int size, int number) {
        List<DonationReceiveRankingDto> result = queryFactory.select(Projections.constructor(DonationReceiveRankingDto.class,
                        donation.id,
                        donation.giver.name,
                        donation.amount.sum().as("totalAmount")
                ))
                .from(donation)
                .where(receiverId(receiverId))
                .groupBy(donation.giver)
                .orderBy(Expressions.stringPath("totalAmount").desc())
                .offset((long) size * (number - 1)) // 페이지 번호 1부터 시작
                .limit(size)
                .fetch();

        Long totalCount = jdbcTemplate.queryForObject("select count(d.giver_id) from (select d.giver_id from donation d where d.receiver_id = ? group by d.giver_id) as d",
                Long.class,
                receiverId
        );

        return new Page<>(result, number, size, totalCount);
    }

    private BooleanExpression receiverId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException(ErrorCode.DONATION_HISTORY_INVALID_RECEIVER_ID);
        }
        return donation.receiver.id.eq(id);
    }

    private BooleanExpression giverIdEq(Long id) {
        if (id == null) {
            throw new IllegalArgumentException(ErrorCode.DONATION_HISTORY_INVALID_GIVER_ID);
        }
        return donation.giver.id.eq(id);
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
