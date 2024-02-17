package com.koreanbrains.onlinebutlerback.repository.post;

import com.koreanbrains.onlinebutlerback.common.scroll.Scroll;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

import static com.koreanbrains.onlinebutlerback.entity.post.QPost.*;
import static com.koreanbrains.onlinebutlerback.entity.tag.QTag.*;
import static com.koreanbrains.onlinebutlerback.entity.tag.QTagMapping.*;

@Repository
public class PostQueryRepository {
    private final JPAQueryFactory queryFactory;

    public PostQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, em);
    }

    public Scroll<PostScrollDto> scrollPost(Long cursor, String tagName, int size) {
        List<PostScrollDto> posts = queryFactory
                .select(Projections.fields(PostScrollDto.class,
                        post.id,
                        post.caption))
                .from(post)
                .leftJoin(tagMapping).on(tagMapping.post.id.eq(post.id))
                .leftJoin(tag).on(tag.id.eq(tagMapping.tag.id))
                .where(postIdLoe(cursor), tagNameEq(tagName))
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

        for (Tuple t : tags) {
            posts.stream()
                    .filter(p -> Objects.equals(p.getId(), t.get(post.id)))
                    .findFirst()
                    .ifPresent(p -> p.addTag(t.get(tag.name)));
        }

        Long nextCursor = null;
        if(posts.size() > size) {
            nextCursor = posts.get(posts.size() - 1).getId();
            posts.remove(posts.size() - 1);
        }

        return new Scroll<>(posts, nextCursor, null);
    }

    private BooleanExpression postIdLoe(Long id) {
        return id == null ? null : post.id.loe(id);
    }

    private BooleanExpression tagNameEq(String tagName) {
        return StringUtils.hasText(tagName) ? tag.name.eq(tagName) : null;
    }
}
