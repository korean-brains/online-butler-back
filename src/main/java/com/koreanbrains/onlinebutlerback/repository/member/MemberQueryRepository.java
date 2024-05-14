package com.koreanbrains.onlinebutlerback.repository.member;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.koreanbrains.onlinebutlerback.entity.follow.QFollow.follow;
import static com.koreanbrains.onlinebutlerback.entity.member.QMember.member;
import static com.koreanbrains.onlinebutlerback.entity.post.QPost.*;

@Repository
public class MemberQueryRepository {
    private final JPAQueryFactory queryFactory;

    public MemberQueryRepository(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Optional<MemberDto> findById(Long myId, Long memberId) {
        MemberDto memberDto = queryFactory.select(Projections.constructor(MemberDto.class,
                        member.id,
                        member.name,
                        member.email,
                        member.profileImage.url,
                        member.introduction,
                        JPAExpressions.select(post.count())
                                .from(post)
                                .where(post.writer.eq(member)),
                        JPAExpressions.select(follow.count())
                                .from(follow)
                                .where(follow.following.eq(member)),
                        JPAExpressions.select(follow.count())
                                .from(follow)
                                .where(follow.follower.eq(member)),
                        JPAExpressions.selectOne()
                                .from(follow)
                                .where(isFollow(myId, member.id))
                                .exists()
                        ))
                .from(member)
                .where(member.id.eq(memberId))
                .fetchOne();

        return memberDto == null ? Optional.empty() : Optional.of(memberDto);
    }

    private BooleanExpression isFollow(Long myId, NumberPath<Long> memberId) {
        if (myId == null) return follow.follower.id.eq(0L) ;
        return follow.follower.id.eq(myId).and(follow.following.id.eq(memberId));
    }
}
