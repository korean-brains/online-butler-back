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

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class TagQueryRepositoryTest {

    @Autowired
    TagQueryRepository tagQueryRepository;

    @Autowired
    TagRepository tagRepository;

    @BeforeEach
    void setup() {
        for (int i = 0; i < 10; i++) {
            tagRepository.save(Tag.builder()
                    .name("태그 " + i)
                    .build());
        }
    }
    
    @Test
    @DisplayName("태그를 검색한다")
    void searchTag() {
        // given
        Long cursor = 0L;
        String name = "태그";
        int size = 5;
        
        // when
        Scroll<Tag> tagScroll = tagQueryRepository.searchTag(cursor, name, size);

        // then
        assertThat(tagScroll.getContent().size()).isEqualTo(size);
        assertThat(tagScroll.getNextCursor()).isEqualTo(size + 1L);
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