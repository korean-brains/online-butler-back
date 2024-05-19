package com.koreanbrains.onlinebutlerback.repository.member;

import com.koreanbrains.onlinebutlerback.common.scroll.Scroll;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    public Scroll<MemberScrollDto> scrollSearchMember(Long cursor, int size, String name) {
        List<MemberScrollDto> members = queryFactory.select(Projections.constructor(MemberScrollDto.class,
                        member.id,
                        member.profileImage.url,
                        member.name,
                        member.introduction
                ))
                .from(member)
                .where(member.name.contains(name), memberIdGoe(cursor))
                .limit(size + 1)
                .fetch();

        Long nextCursor = null;
        if (members.size() > size) {
            nextCursor = members.get(members.size() - 1).id();
            members.remove(members.size() - 1);
        }

        return new Scroll<>(members, nextCursor, null);
    }

    private BooleanExpression memberIdGoe(Long id) {
        return id == null ? null : member.id.goe(id);
    }

    private BooleanExpression isFollow(Long myId, NumberPath<Long> memberId) {
        if (myId == null) return follow.follower.id.eq(0L) ;
        return follow.follower.id.eq(myId).and(follow.following.id.eq(memberId));
    }
}
