package com.koreanbrains.onlinebutlerback.repository.post;

import com.koreanbrains.onlinebutlerback.common.scroll.Scroll;
import com.koreanbrains.onlinebutlerback.entity.like.Like;
import com.koreanbrains.onlinebutlerback.entity.member.Member;
import com.koreanbrains.onlinebutlerback.entity.post.Post;
import com.koreanbrains.onlinebutlerback.entity.post.PostImage;
import com.koreanbrains.onlinebutlerback.entity.tag.Tag;
import com.koreanbrains.onlinebutlerback.entity.tag.TagMapping;
import com.koreanbrains.onlinebutlerback.repository.like.LikeRepository;
import com.koreanbrains.onlinebutlerback.repository.member.MemberRepository;
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
import java.util.Optional;

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
    LikeRepository likeRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PostImageRepository postImageRepository;
    @Autowired
    EntityManager em;

    Member member;
    List<Post> posts;

    @BeforeEach
    void setup() {
        em.createNativeQuery("ALTER TABLE post ALTER COLUMN id RESTART with 1").executeUpdate();
        member = memberRepository.save(Member.builder().name("kim").email("kim@gmail.com").isActive(true).build());

        List<Tag> tags = tagRepository.saveAll(List.of(
                Tag.builder().name("태그 1").build(),
                Tag.builder().name("태그 2").build()
        ));

        posts = postRepository.saveAll(List.of(
                Post.builder().caption("포스트 1").writer(member).build(),
                Post.builder().caption("포스트 2").writer(member).build(),
                Post.builder().caption("포스트 3").writer(member).build(),
                Post.builder().caption("포스트 4").writer(member).build(),
                Post.builder().caption("포스트 5").writer(member).build(),
                Post.builder().caption("포스트 6").writer(member).build(),
                Post.builder().caption("포스트 7").writer(member).build(),
                Post.builder().caption("포스트 8").writer(member).build(),
                Post.builder().caption("포스트 9").writer(member).build(),
                Post.builder().caption("포스트 10").writer(member).build()
        ));

        for (Post post : posts) {
            for (Tag tag : tags) {
                tagMappingRepository.save(TagMapping.builder().tag(tag).post(post).build());
            }
            likeRepository.save(Like.builder().post(post).member(member).build());
            postImageRepository.save(PostImage.builder().post(post).originalName("image.jpg").storedName("image.jpg").url("/assets/image.jpg").build());
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
        Scroll<PostScrollDto> result = postQueryRepository.scrollPost(1L, cursor, tagName, size);

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
        Scroll<PostScrollDto> result = postQueryRepository.scrollPost(1L, cursor, tagName, size);

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
        Scroll<PostScrollDto> result = postQueryRepository.scrollPost(1L, cursor, tagName, size);

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
    @DisplayName("특정 사용자가 작성한 포스트 목록을 무한스크롤로 조회한다")
    void scrollPostWriter() {
        // given
        Long cursor = null;
        Long writerId = member.getId();
        int size = 5;

        // when
        Scroll<PostScrollDto> result = postQueryRepository.scrollPost(1L, cursor, size, writerId);

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
    @DisplayName("좋아요한 게시글 목록을 무한스크롤로 조회한다")
    void scrollLikePost() {
        // given
        Long cursor = null;
        Long memberId = member.getId();
        int size = 5;

        // when
        Scroll<PostScrollDto> result = postQueryRepository.scrollLikePost(1L, cursor, memberId, size);

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
    @DisplayName("좋아요한 게시글 목록 무한스크롤의 마지막 페이지를 조회한다")
    void scrollLikePostLastPage() {
        // given
        Long cursor = 5L;
        Long memberId = member.getId();
        int size = 5;

        // when
        Scroll<PostScrollDto> result = postQueryRepository.scrollLikePost(1L, cursor, memberId, size);

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
    @DisplayName("게시글을 단건 조회한다")
    void findById() {
        // given
        Post post = posts.get(0);
        Long postId = post.getId();

        // when
        Optional<PostDto> postDto = postQueryRepository.findById(1L, postId);

        // then
        assertThat(postDto.isPresent()).isTrue();
        assertThat(postDto.get().getId()).isEqualTo(post.getId());
    }

    @Test
    @DisplayName("게시글을 단건 조회 결과가 없으면 Optional.empty()를 반환한다")
    void findByIdEmpty() {
        // given
        Long postId = 0L;

        // when
        Optional<PostDto> postDto = postQueryRepository.findById(1L, postId);

        // then
        assertThat(postDto.isPresent()).isFalse();
    }

    @Test
    @DisplayName("미인증 사용자가 게시글 단건 조회시 작성자와 언팔로우 상태로 조회된다.")
    void findByIdWithoutAuth() {
        // given
        Long postId = 1L;

        // when
        Optional<PostDto> postDto = postQueryRepository.findById(null, postId);

        // then
        assertThat(postDto.isPresent()).isTrue();
        assertThat(postDto.get().getWriter().isFollowed()).isFalse();
    }


    @TestConfiguration
    static class Config {
        @Bean
        public PostQueryRepository postQueryRepository(EntityManager em) {
            return new PostQueryRepository(em);
        }
    }

}