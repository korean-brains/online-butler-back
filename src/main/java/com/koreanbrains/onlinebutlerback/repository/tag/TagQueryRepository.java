package com.koreanbrains.onlinebutlerback.repository.tag;

import com.koreanbrains.onlinebutlerback.common.scroll.Scroll;
import com.koreanbrains.onlinebutlerback.entity.tag.Tag;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.koreanbrains.onlinebutlerback.entity.tag.QTag.*;

@Repository
public class TagQueryRepository {

    private final JPAQueryFactory queryFactory;

    public TagQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Scroll<Tag> searchTag(Long cursor, String name, int size) {
        List<Tag> tags = queryFactory.select(tag)
                .from(tag)
                .where(tag.name.contains(name),
                        idGoe(cursor))
                .limit(size + 1)
                .fetch();

        Long nextCursor = null;
        if(tags.size() > size) {
            nextCursor = tags.get(tags.size() - 1).getId();
            tags.remove(tags.size() - 1);
        }

        return new Scroll<>(tags, nextCursor, null);
    }

    private BooleanExpression idGoe(Long id) {
        return id == null ? null : tag.id.goe(id);
    }
}
