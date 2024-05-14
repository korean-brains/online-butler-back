package com.koreanbrains.onlinebutlerback.repository.follow;

import com.koreanbrains.onlinebutlerback.common.scroll.Scroll;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.koreanbrains.onlinebutlerback.entity.follow.QFollow.follow;
import static com.koreanbrains.onlinebutlerback.entity.member.QMember.member;

@Repository
public class FollowQueryRepository {

    public final JPAQueryFactory queryFactory;

    public FollowQueryRepository(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Scroll<FollowDto> findFollowingList(long memberId, Long cursor, int size) {
        List<FollowDto> followingList = queryFactory.select(Projections.constructor(FollowDto.class,
                        follow.id,
                        member.id,
                        member.name,
                        member.profileImage.url
                ))
                .from(follow)
                .join(follow.following, member)
                .where(follow.follower.id.eq(memberId), followIdGoe(cursor))
                .limit(size + 1)
                .fetch();

        Long nextCursor = null;
        if (followingList.size() > size) {
            nextCursor = followingList.get(followingList.size() - 1).id();
            followingList.remove(followingList.size() - 1);
        }

        return new Scroll<>(followingList, nextCursor, null);
    }

    public Scroll<FollowDto> findFollowerList(long memberId, Long cursor, int size) {
        List<FollowDto> followerList = queryFactory.select(Projections.constructor(FollowDto.class,
                        follow.id,
                        member.id,
                        member.name,
                        member.profileImage.url
                ))
                .from(follow)
                .join(follow.follower, member)
                .where(follow.following.id.eq(memberId), followIdGoe(cursor))
                .limit(size + 1)
                .fetch();

        Long nextCursor = null;
        if (followerList.size() > size) {
            nextCursor = followerList.get(followerList.size() - 1).id();
            followerList.remove(followerList.size() - 1);
        }

        return new Scroll<>(followerList, nextCursor, null);
    }

    private BooleanExpression followIdGoe(Long followId) {
        return followId == null ? null : follow.id.goe(followId);
    }
}
