package com.koreanbrains.onlinebutlerback.repository.comment;

import com.koreanbrains.onlinebutlerback.common.scroll.Scroll;
import com.koreanbrains.onlinebutlerback.entity.comment.QComment;
import com.koreanbrains.onlinebutlerback.entity.member.QMember;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
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

    public Scroll<CommentScrollDto> scrollComment(Long postId, Long cursor, int size) {
        List<CommentScrollDto> comments = queryFactory.select(Projections.constructor(CommentScrollDto.class,
                        comment.id,
                        comment.text,
                        member.name,
                        member.profileImage.url,
                        comment.createdAt
                ))
                .from(comment)
                .join(comment.author, member)
                .where(comment.post.id.eq(postId),
                        comment.root.isNull(),
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

    // TODO : select 프로필 이미지 추가
    public Scroll<ReplyScrollDto> scrollReply(Long commentId, Long cursor, int size) {
        QComment reply = new QComment("reply");
        QComment parent = new QComment("parent");
        QMember replyAuthor = new QMember("replyAuthor");
        QMember parentAuthor = new QMember("parentAuthor");

        List<ReplyScrollDto> replies = queryFactory.select(Projections.constructor(ReplyScrollDto.class,
                        reply.id,
                        reply.text,
                        replyAuthor.name,
                        replyAuthor.profileImage.url,
                        parentAuthor.name,
                        parentAuthor.profileImage.url,
                        reply.createdAt
                ))
                .from(reply)
                .join(reply.author, replyAuthor)
                .join(reply.parent, parent)
                .join(parent.author, parentAuthor)
                .where(reply.root.id.eq(commentId),
                        commentIdGoe(reply, cursor))
                .limit(size + 1)
                .fetch();

        Long nextCursor = null;
        if (replies.size() > size) {
            nextCursor = replies.get(replies.size() - 1).id();
            replies.remove(replies.size() - 1);
        }

        return new Scroll<>(replies, nextCursor, null);
    }

    private BooleanExpression commentIdGoe(Long cursor) {
        return cursor == null ? null : comment.id.goe(cursor);
    }
    private BooleanExpression commentIdGoe(QComment comment, Long cursor) {
        return cursor == null ? null : comment.id.goe(cursor);
    }
}
