package com.koreanbrains.onlinebutlerback.repository.comment;

import com.koreanbrains.onlinebutlerback.common.scroll.Scroll;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.koreanbrains.onlinebutlerback.entity.comment.QComment.*;
import static com.koreanbrains.onlinebutlerback.entity.member.QMember.*;

@Repository
public class CommentQueryRepository {

    private final JPAQueryFactory queryFactory;

    public CommentQueryRepository(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    // TODO : select 프로필 이미지 추가
    public Scroll<CommentScrollDto> scrollComment(Long postId, Long cursor, int size) {
        List<CommentScrollDto> comments = queryFactory.select(Projections.constructor(CommentScrollDto.class,
                        comment.id,
                        comment.text,
                        member.name,
                        Expressions.constant("profile image"),
                        comment.createdAt
                ))
                .from(comment)
                .join(comment.author, member)
                .where(comment.post.id.eq(postId),
                        commentIdGoe(cursor))
                .limit(size + 1)
                .fetch();

        Long nextCursor = null;
        if (comments.size() > size) {
            nextCursor = comments.get(comments.size() - 1).id();
            comments.remove(comments.size() - 1);
        }

        return new Scroll<>(comments, nextCursor, null);
    }

    private BooleanExpression commentIdGoe(Long cursor) {
        return cursor == null ? null : comment.id.goe(cursor);
    }
}
