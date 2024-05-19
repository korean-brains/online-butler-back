package com.koreanbrains.onlinebutlerback.repository.tag;

import com.koreanbrains.onlinebutlerback.common.scroll.Scroll;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.koreanbrains.onlinebutlerback.entity.post.QPost.post;
import static com.koreanbrains.onlinebutlerback.entity.tag.QTag.*;
import static com.koreanbrains.onlinebutlerback.entity.tag.QTagMapping.tagMapping;

@Repository
public class TagQueryRepository {

    private final JPAQueryFactory queryFactory;

    public TagQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Scroll<TagScrollDto> searchTag(Long cursor, String name, int size) {
        List<TagScrollDto> tags = queryFactory.select(Projections.constructor(TagScrollDto.class,
                        tag.id,
                        tag.name,
                        JPAExpressions.select(post.count())
                                .from(post)
                                .join(tagMapping).on(tagMapping.post.eq(post))
                                .where(tagMapping.tag.eq(tag))
                ))
                .from(tag)
                .where(tag.name.contains(name),
                        idGoe(cursor))
                .limit(size + 1)
                .fetch();

        Long nextCursor = null;
        if (tags.size() > size) {
            nextCursor = tags.get(tags.size() - 1).id();
            tags.remove(tags.size() - 1);
        }

        return new Scroll<>(tags, nextCursor, null);
    }

    private BooleanExpression idGoe(Long id) {
        return id == null ? tag.id.goe(0) : tag.id.goe(id);
    }
}
