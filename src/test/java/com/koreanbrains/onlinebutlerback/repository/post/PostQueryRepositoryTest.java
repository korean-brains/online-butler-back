package com.koreanbrains.onlinebutlerback.repository.post;

import com.koreanbrains.onlinebutlerback.common.scroll.Scroll;
import com.koreanbrains.onlinebutlerback.entity.post.Post;
import com.koreanbrains.onlinebutlerback.entity.tag.Tag;
import com.koreanbrains.onlinebutlerback.entity.tag.TagMapping;
import com.koreanbrains.onlinebutlerback.repository.tag.TagMappingRepository;
import com.koreanbrains.onlinebutlerback.repository.tag.TagRepository;
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
class PostQueryRepositoryTest {

    @Autowired
    PostQueryRepository postQueryRepository;

    @Autowired
    PostRepository postRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    TagMappingRepository tagMappingRepository;
    @Autowired
    EntityManager em;

    @BeforeEach
    void setup() {
        em.createNativeQuery("ALTER TABLE post ALTER COLUMN id RESTART with 1").executeUpdate();

        List<Tag> tags = tagRepository.saveAll(List.of(
                Tag.builder().name("태그 1").build(),
                Tag.builder().name("태그 2").build()
        ));

        List<Post> posts = postRepository.saveAll(List.of(
                Post.builder().caption("포스트 1").memberId(1L).build(),
                Post.builder().caption("포스트 2").memberId(1L).build(),
                Post.builder().caption("포스트 3").memberId(1L).build(),
                Post.builder().caption("포스트 4").memberId(1L).build(),
                Post.builder().caption("포스트 5").memberId(1L).build(),
                Post.builder().caption("포스트 6").memberId(1L).build(),
                Post.builder().caption("포스트 7").memberId(1L).build(),
                Post.builder().caption("포스트 8").memberId(1L).build(),
                Post.builder().caption("포스트 9").memberId(1L).build(),
                Post.builder().caption("포스트 10").memberId(1L).build()
        ));

        for (Post post : posts) {
            for (Tag tag : tags) {
                tagMappingRepository.save(TagMapping.builder().tag(tag).post(post).build());
            }
        }
    }


    @Test
    @DisplayName("포스트 목록을 무한스크롤로 조회한다")
    void scrollPost() {
        // given
        Long cursor = null;
        String tagName = null;
        int size = 5;

        // when
        Scroll<PostScrollDto> result = postQueryRepository.scrollPost(cursor, tagName, size);

        // then
        assertThat(result.getNextCursor()).isEqualTo(5L);
        assertThat(result.getNextSubCursor()).isNull();
        assertThat(result.getContent().size()).isEqualTo(size);
        assertThat(result.getContent().get(0).getCaption()).isEqualTo("포스트 10");
        assertThat(result.getContent().get(1).getCaption()).isEqualTo("포스트 9");
        assertThat(result.getContent().get(2).getCaption()).isEqualTo("포스트 8");
        assertThat(result.getContent().get(3).getCaption()).isEqualTo("포스트 7");
        assertThat(result.getContent().get(4).getCaption()).isEqualTo("포스트 6");
    }

    @Test
    @DisplayName("포스트 목록 무한스크롤의 마지막 페이지를 조회한다")
    void scrollPostLastPage() {
        // given
        Long cursor = 5L;
        String tagName = null;
        int size = 5;

        // when
        Scroll<PostScrollDto> result = postQueryRepository.scrollPost(cursor, tagName, size);

        // then
        assertThat(result.getNextCursor()).isNull();
        assertThat(result.getNextSubCursor()).isNull();
        assertThat(result.getContent().size()).isEqualTo(size);
        assertThat(result.getContent().get(0).getCaption()).isEqualTo("포스트 5");
        assertThat(result.getContent().get(1).getCaption()).isEqualTo("포스트 4");
        assertThat(result.getContent().get(2).getCaption()).isEqualTo("포스트 3");
        assertThat(result.getContent().get(3).getCaption()).isEqualTo("포스트 2");
        assertThat(result.getContent().get(4).getCaption()).isEqualTo("포스트 1");
    }

    @Test
    @DisplayName("특정 해시태그가 있는 포스트 목록을 무한스크롤로 조회한다")
    void scrollPostTag() {
        // given
        Long cursor = null;
        String tagName = "태그 1";
        int size = 5;

        // when
        Scroll<PostScrollDto> result = postQueryRepository.scrollPost(cursor, tagName, size);

        // then
        assertThat(result.getNextCursor()).isEqualTo(5L);
        assertThat(result.getNextSubCursor()).isNull();
        assertThat(result.getContent().size()).isEqualTo(size);
        assertThat(result.getContent().get(0).getCaption()).isEqualTo("포스트 10");
        assertThat(result.getContent().get(1).getCaption()).isEqualTo("포스트 9");
        assertThat(result.getContent().get(2).getCaption()).isEqualTo("포스트 8");
        assertThat(result.getContent().get(3).getCaption()).isEqualTo("포스트 7");
        assertThat(result.getContent().get(4).getCaption()).isEqualTo("포스트 6");
    }


    @TestConfiguration
    static class Config {
        @Bean
        public PostQueryRepository postQueryRepository(EntityManager em) {
            return new PostQueryRepository(em);
        }
    }

}