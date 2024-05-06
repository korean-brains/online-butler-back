package com.koreanbrains.onlinebutlerback.repository.member;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.koreanbrains.onlinebutlerback.entity.member.QMember.member;
import static com.koreanbrains.onlinebutlerback.entity.post.QPost.*;

@Repository
public class MemberQueryRepository {
    private final JPAQueryFactory queryFactory;

    public MemberQueryRepository(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Optional<MemberDto> findById(Long memberId) {
        MemberDto memberDto = queryFactory.select(Projections.constructor(MemberDto.class,
                        member.id,
                        member.name,
                        member.email,
                        member.profileImage.url,
                        JPAExpressions.select(post.count())
                                .from(post)
                                .where(post.writer.eq(member)),
                        Expressions.constant(0L),
                        Expressions.constant(0L)
                ))
                .from(member)
                .where(member.id.eq(memberId))
                .fetchOne();

        return memberDto == null ? Optional.empty() : Optional.of(memberDto);
    }
}
