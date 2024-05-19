package com.koreanbrains.onlinebutlerback.repository.tag;

import com.koreanbrains.onlinebutlerback.common.scroll.Scroll;
import com.koreanbrains.onlinebutlerback.entity.tag.Tag;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class TagQueryRepositoryTest {

    @Autowired
    TagQueryRepository tagQueryRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    EntityManager em;

    @BeforeEach
    void setup() {
        em.createNativeQuery("ALTER TABLE tag ALTER COLUMN id RESTART with 1").executeUpdate();
        tagRepository.saveAll(List.of(
                Tag.builder().name("태그 1").build(),
                Tag.builder().name("태그 2").build(),
                Tag.builder().name("태그 3").build(),
                Tag.builder().name("태그 4").build(),
                Tag.builder().name("태그 5").build(),
                Tag.builder().name("태그 6").build(),
                Tag.builder().name("태그 7").build(),
                Tag.builder().name("태그 8").build(),
                Tag.builder().name("태그 9").build(),
                Tag.builder().name("태그 10").build()
        ));
    }

    @Test
    @DisplayName("태그를 검색한다")
    void searchTag() {
        // given
        Long cursor = null;
        String name = "태그";
        int size = 5;
        
        // when
        Scroll<TagScrollDto> tagScroll = tagQueryRepository.searchTag(cursor, name, size);

        // then
        assertThat(tagScroll.getContent().size()).isEqualTo(size);
        assertThat(tagScroll.getNextCursor()).isEqualTo(size + 1L);
        assertThat(tagScroll.getNextSubCursor()).isNull();
    }

    @Test
    @DisplayName("태그 검색 결과 무한스크롤의 마지막 페이지를 조회한다")
    void searchTagLastPage() {
        // given
        Long cursor = 6L;
        String name = "태그";
        int size = 5;

        // when
        Scroll<TagScrollDto> tagScroll = tagQueryRepository.searchTag(cursor, name, size);

        // then
        assertThat(tagScroll.getContent().size()).isEqualTo(size);
        assertThat(tagScroll.getNextCursor()).isNull();
        assertThat(tagScroll.getNextSubCursor()).isNull();
    }

    @TestConfiguration
    static class Config {
        @Bean
        public TagQueryRepository tagQueryRepository(EntityManager em) {
            return new TagQueryRepository(em);
        }
    }
}