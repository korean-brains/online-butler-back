package com.koreanbrains.onlinebutlerback.repository.post;

import com.koreanbrains.onlinebutlerback.common.scroll.Scroll;
import com.koreanbrains.onlinebutlerback.entity.post.PostImage;
import com.querydsl.core.Tuple;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.koreanbrains.onlinebutlerback.entity.comment.QComment.*;
import static com.koreanbrains.onlinebutlerback.entity.follow.QFollow.follow;
import static com.koreanbrains.onlinebutlerback.entity.like.QLike.*;
import static com.koreanbrains.onlinebutlerback.entity.member.QMember.*;
import static com.koreanbrains.onlinebutlerback.entity.post.QPost.*;
import static com.koreanbrains.onlinebutlerback.entity.post.QPostImage.*;
import static com.koreanbrains.onlinebutlerback.entity.tag.QTag.*;
import static com.koreanbrains.onlinebutlerback.entity.tag.QTagMapping.*;

@Repository
public class PostQueryRepository {
    private final JPAQueryFactory queryFactory;

    public PostQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, em);
    }

    public Scroll<PostScrollDto> scrollPost(Long myId, Long cursor, int size, Long writerId) {
        return scrollPost(myId, cursor, null, size, writerId, null);
    }

    public Scroll<PostScrollDto> scrollPost(Long myId, Long cursor, String tagName, int size) {
        return scrollPost(myId, cursor, tagName, size, null, null);
    }

    public Scroll<PostScrollDto> scrollLikePost(Long myId, Long cursor, Long memberId, int size) {
        return scrollPost(myId, cursor, null, size, null, memberId);
    }

    public Scroll<PostScrollDto> scrollPost(Long myId, Long cursor, String tagName, int size, Long writerId, Long likeMemberId) {
        List<PostScrollDto> posts = queryFactory
                .select(Projections.constructor(PostScrollDto.class,
                        post.id,
                        post.caption,
                        post.createdAt,
                        JPAExpressions.select(like.count())
                                .from(like)
                                .where(like.post.eq(post)),
                        JPAExpressions.select(comment.count())
                                .from(comment)
                                .where(comment.post.eq(post)),
                        Projections.constructor(PostScrollDto.Writer.class,
                                member.id,
                                member.name,
                                member.profileImage.url,
                                JPAExpressions.selectOne()
                                        .from(follow)
                                        .where(isFollow(myId, member.id))
                                        .exists()
                        )
                ))
                .from(post)
                .join(post.writer, member)
                .leftJoin(tagMapping).on(tagMapping.post.id.eq(post.id))
                .leftJoin(tag).on(tag.id.eq(tagMapping.tag.id))
                .leftJoin(like).on(like.post.id.eq(post.id))
                .where(postIdLoe(cursor), tagNameEq(tagName), writerIdEq(writerId), likeMemberIdEq(likeMemberId))
                .groupBy(post.id)
                .orderBy(post.id.desc())
                .limit(size + 1)
                .fetch();

        List<Long> postIds = posts.stream()
                .map(PostScrollDto::getId)
                .toList();

        List<Tuple> tags = queryFactory
                .select(tag.name, post.id)
                .from(tag)
                .join(tagMapping).on(tagMapping.tag.id.eq(tag.id))
                .where(tagMapping.post.id.in(postIds))
                .fetch();


        List<PostImage> postImages = queryFactory
                .select(postImage)
                .from(postImage)
                .where(postImage.post.id.in(postIds))
                .fetch();

        for (Tuple t : tags) {
            posts.stream()
                    .filter(p -> Objects.equals(p.getId(), t.get(post.id)))
                    .findFirst()
                    .ifPresent(p -> p.addTag(t.get(tag.name)));
        }
        postImages.forEach(image -> posts.stream()
                .filter(p -> Objects.equals(p.getId(), image.getPost().getId()))
                .findFirst()
                .ifPresent(p -> p.addImage(image.getUrl()))
        );

        Long nextCursor = null;
        if (posts.size() > size) {
            nextCursor = posts.get(posts.size() - 1).getId();
            posts.remove(posts.size() - 1);
        }

        return new Scroll<>(posts, nextCursor, null);
    }

    public Optional<PostDto> findById(Long myId, Long postId) {
        Map<Long, PostDto> result = queryFactory
                .from(post)
                .join(post.writer, member)
                .leftJoin(tagMapping).on(tagMapping.post.id.eq(post.id))
                .leftJoin(tag).on(tag.id.eq(tagMapping.tag.id))
                .where(post.id.eq(postId))
                .transform(GroupBy.groupBy(post.id).as(
                        Projections.fields(PostDto.class,
                                post.id,
                                post.caption,
                                post.createdAt,
                                Expressions.as(JPAExpressions.select(like.count())
                                        .from(like)
                                        .where(like.post.eq(post)), "likeCount"),
                                Expressions.as(JPAExpressions.select(comment.count())
                                        .from(comment)
                                        .where(comment.post.eq(post)), "commentCount"),
                                Expressions.as(Projections.constructor(PostDto.Writer.class,
                                        member.id,
                                        member.name,
                                        member.profileImage.url,
                                        JPAExpressions.selectOne()
                                                .from(follow)
                                                .where(isFollow(myId, member.id))
                                                .exists()
                                ), "writer"),
                                GroupBy.list(tag.name).as("tags")
                        )));

        if(result.isEmpty()) return Optional.empty();
        PostDto postDto = result.get(postId);

        List<String> postImages = queryFactory
                .select(postImage.url)
                .from(postImage)
                .where(postImage.post.id.eq(postId))
                .fetch();

        postDto.setImages(postImages);

        return Optional.of(result.get(postId));
    }

    private BooleanExpression postIdLoe(Long id) {
        return id == null ? null : post.id.loe(id);
    }

    private BooleanExpression tagNameEq(String tagName) {
        return StringUtils.hasText(tagName) ? tag.name.eq(tagName) : null;
    }

    private BooleanExpression writerIdEq(Long writerId) {
        return writerId == null ? null : post.writer.id.eq(writerId);
    }

    private BooleanExpression likeMemberIdEq(Long memberId) {
        return memberId == null ? null : like.member.id.eq(memberId);
    }

    private BooleanExpression isFollow(Long myId, NumberPath<Long> memberId) {
        if(myId == null) return null;
        return follow.follower.id.eq(myId).and(follow.following.id.eq(memberId));
    }
}
