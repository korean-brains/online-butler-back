package com.koreanbrains.onlinebutlerback.repository.comment;

import com.koreanbrains.onlinebutlerback.common.scroll.Scroll;
import com.koreanbrains.onlinebutlerback.entity.comment.Comment;
import com.koreanbrains.onlinebutlerback.entity.member.Member;
import com.koreanbrains.onlinebutlerback.entity.post.Post;
import com.koreanbrains.onlinebutlerback.repository.member.MemberRepository;
import com.koreanbrains.onlinebutlerback.repository.post.PostRepository;
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
class CommentQueryRepositoryTest {

    @Autowired
    CommentQueryRepository commentQueryRepository;

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    EntityManager em;

    Post post;
    Comment comment;
    List<Comment> comments;
    List<Comment> replies;

    @BeforeEach
    void setup() {
        Member member = memberRepository.save(Member.builder().name("kim").email("kim@gmail.com").isActive(true).build());
        post = postRepository.save(Post.builder().caption("포스트 1").memberId(member.getId()).build());
        comments = commentRepository.saveAll(List.of(
                Comment.builder().text("댓글 1").author(member).post(post).build(),
                Comment.builder().text("댓글 2").author(member).post(post).build(),
                Comment.builder().text("댓글 3").author(member).post(post).build(),
                Comment.builder().text("댓글 4").author(member).post(post).build(),
                Comment.builder().text("댓글 5").author(member).post(post).build(),
                Comment.builder().text("댓글 6").author(member).post(post).build(),
                Comment.builder().text("댓글 7").author(member).post(post).build(),
                Comment.builder().text("댓글 8").author(member).post(post).build(),
                Comment.builder().text("댓글 9").author(member).post(post).build(),
                Comment.builder().text("댓글 10").author(member).post(post).build()
        ));

        comment = comments.get(0);
        replies = commentRepository.saveAll(List.of(
                Comment.builder().text("답글 1").author(member).parent(comment).root(comment).build(),
                Comment.builder().text("답글 2").author(member).parent(comment).root(comment).build(),
                Comment.builder().text("답글 3").author(member).parent(comment).root(comment).build(),
                Comment.builder().text("답글 4").author(member).parent(comment).root(comment).build(),
                Comment.builder().text("답글 5").author(member).parent(comment).root(comment).build(),
                Comment.builder().text("답글 6").author(member).parent(comment).root(comment).build(),
                Comment.builder().text("답글 7").author(member).parent(comment).root(comment).build(),
                Comment.builder().text("답글 8").author(member).parent(comment).root(comment).build(),
                Comment.builder().text("답글 9").author(member).parent(comment).root(comment).build(),
                Comment.builder().text("답글 10").author(member).parent(comment).root(comment).build()
        ));
    }

    @Test
    @DisplayName("댓글 목록을 무한스크롤로 조회한다")
    void scrollComment() {
        // given
        int size = 5;
        Long cursor = null;

        // when
        Scroll<CommentScrollDto> result = commentQueryRepository.scrollComment(post.getId(), cursor, size);

        // then
        assertThat(result.getNextCursor()).isEqualTo(comments.get(size).getId());
        assertThat(result.getNextSubCursor()).isNull();
        assertThat(result.getContent().size()).isEqualTo(5);
        assertThat(result.getContent().get(0).text()).isEqualTo("댓글 1");
        assertThat(result.getContent().get(1).text()).isEqualTo("댓글 2");
        assertThat(result.getContent().get(2).text()).isEqualTo("댓글 3");
        assertThat(result.getContent().get(3).text()).isEqualTo("댓글 4");
        assertThat(result.getContent().get(4).text()).isEqualTo("댓글 5");
    }

    @Test
    @DisplayName("댓글 목록 무한스크롤 마지막 페이지를 조회한다")
    void scrollCommentLastPage() {
        // given
        int size = 5;
        Long cursor = comments.get(size).getId();

        // when
        Scroll<CommentScrollDto> result = commentQueryRepository.scrollComment(post.getId(), cursor, size);

        // then
        assertThat(result.getNextCursor()).isNull();
        assertThat(result.getNextSubCursor()).isNull();
        assertThat(result.getContent().size()).isEqualTo(5);
        assertThat(result.getContent().get(0).text()).isEqualTo("댓글 6");
        assertThat(result.getContent().get(1).text()).isEqualTo("댓글 7");
        assertThat(result.getContent().get(2).text()).isEqualTo("댓글 8");
        assertThat(result.getContent().get(3).text()).isEqualTo("댓글 9");
        assertThat(result.getContent().get(4).text()).isEqualTo("댓글 10");
    }

    @Test
    @DisplayName("답글 목록을 무한스크롤로 조회한다")
    void scrollReply() {
        // given
        int size = 5;
        Long cursor = null;

        // when
        Scroll<ReplyScrollDto> result = commentQueryRepository.scrollReply(comment.getId(), cursor, size);

        // then
        assertThat(result.getNextCursor()).isEqualTo(replies.get(size).getId());
        assertThat(result.getNextSubCursor()).isNull();
        assertThat(result.getContent().size()).isEqualTo(5);
        assertThat(result.getContent().get(0).text()).isEqualTo("답글 1");
        assertThat(result.getContent().get(1).text()).isEqualTo("답글 2");
        assertThat(result.getContent().get(2).text()).isEqualTo("답글 3");
        assertThat(result.getContent().get(3).text()).isEqualTo("답글 4");
        assertThat(result.getContent().get(4).text()).isEqualTo("답글 5");
    }

    @Test
    @DisplayName("답글 목록 무한스크롤 마지막 페이지를 조회한다")
    void scrollReplyLastPage() {
        // given
        int size = 5;
        Long cursor = replies.get(size).getId();

        // when
        Scroll<ReplyScrollDto> result = commentQueryRepository.scrollReply(comment.getId(), cursor, size);

        // then
        assertThat(result.getNextCursor()).isNull();
        assertThat(result.getNextSubCursor()).isNull();
        assertThat(result.getContent().size()).isEqualTo(5);
        assertThat(result.getContent().get(0).text()).isEqualTo("답글 6");
        assertThat(result.getContent().get(1).text()).isEqualTo("답글 7");
        assertThat(result.getContent().get(2).text()).isEqualTo("답글 8");
        assertThat(result.getContent().get(3).text()).isEqualTo("답글 9");
        assertThat(result.getContent().get(4).text()).isEqualTo("답글 10");
    }


    @TestConfiguration
    static class Config {
        @Bean
        public CommentQueryRepository commentQueryRepository(EntityManager em) {
            return new CommentQueryRepository(em);
        }
    }

}
