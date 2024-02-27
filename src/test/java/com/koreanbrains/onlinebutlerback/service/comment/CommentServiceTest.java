package com.koreanbrains.onlinebutlerback.service.comment;

import com.koreanbrains.onlinebutlerback.common.exception.EntityNotFoundException;
import com.koreanbrains.onlinebutlerback.common.exception.PermissionDeniedException;
import com.koreanbrains.onlinebutlerback.common.fixtures.CommentFixture;
import com.koreanbrains.onlinebutlerback.common.fixtures.MemberFixture;
import com.koreanbrains.onlinebutlerback.common.fixtures.PostFixture;
import com.koreanbrains.onlinebutlerback.entity.comment.Comment;
import com.koreanbrains.onlinebutlerback.entity.member.Member;
import com.koreanbrains.onlinebutlerback.entity.post.Post;
import com.koreanbrains.onlinebutlerback.repository.comment.CommentRepository;
import com.koreanbrains.onlinebutlerback.repository.member.MemberRepository;
import com.koreanbrains.onlinebutlerback.repository.post.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;


@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks
    CommentService commentService;

    @Mock
    CommentRepository commentRepository;
    @Mock
    PostRepository postRepository;
    @Mock
    MemberRepository memberRepository;

    @Test
    @DisplayName("댓글을 작성한다")
    void writeComment() {
        // given
        Post post = PostFixture.post();
        Member author = MemberFixture.member();
        String text = "댓글 내용";

        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(author));
        given(commentRepository.save(any())).willReturn(Comment.builder().id(1L).build());

        // when
        Long commentId = commentService.writeComment(post.getId(), author.getId(), text);

        // then
        assertThat(commentId).isEqualTo(1L);
    }

    @Test
    @DisplayName("댓글 작성시 포스트가 존재하지 않으면 예외가 발생한다")
    void writeCommentFailPost() {
        // given
        Post post = PostFixture.post();
        Member author = MemberFixture.member();
        String text = "댓글 내용";
        given(postRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> commentService.writeComment(post.getId(), author.getId(), text))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("댓글 작성시 작성자가 존재하지 않으면 예외가 발생한다")
    void writeCommentFailAuthor() {
        // given
        Post post = PostFixture.post();
        Member author = MemberFixture.member();
        String text = "댓글 내용";
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> commentService.writeComment(post.getId(), author.getId(), text))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("댓글을 삭제한다")
    void deleteComment() {
        // given
        Comment comment = CommentFixture.comment();
        given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));
        doNothing().when(commentRepository).delete(any());
        doNothing().when(commentRepository).deleteByRoot(any());

        // when
        commentService.deleteComment(comment.getId(), comment.getAuthor().getId());

        // then
        then(commentRepository).should().delete(comment);
    }

    @Test
    @DisplayName("삭제하려는 댓글이 존재하지 않으면 예외가 발생한다")
    void deleteCommentFail() {
        // given
        Comment comment = CommentFixture.comment();
        given(commentRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> commentService.deleteComment(comment.getId(), comment.getAuthor().getId()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("댓글을 삭제하려는 사람과 작성자가 다르면 예외가 발생한다")
    void deleteCommentFailDiffMember() {
        // given
        Comment comment = CommentFixture.comment();
        given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));

        // when
        // then
        assertThatThrownBy(() -> commentService.deleteComment(comment.getId(), comment.getAuthor().getId() + 1))
                .isInstanceOf(PermissionDeniedException.class);
    }

    @Test
    @DisplayName("답글을 작성한다")
    void writeReply() {
        // given
        Comment comment = CommentFixture.comment();
        Member author = MemberFixture.member();
        String text = "답글 내용";

        given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(author));
        given(commentRepository.save(any())).willReturn(Comment.builder().id(1L).build());

        // when
        Long commentId = commentService.writeReply(comment.getId(), author.getId(), text);

        // then
        assertThat(commentId).isEqualTo(1L);
    }

    @Test
    @DisplayName("답글에 답글을 작성한다")
    void writeReplyOfReply() {
        // given
        Comment reply = CommentFixture.reply(CommentFixture.comment());
        Member author = MemberFixture.member();
        String text = "답글 내용";

        given(commentRepository.findById(anyLong())).willReturn(Optional.of(reply));
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(author));
        given(commentRepository.save(any())).willReturn(Comment.builder().id(3L).build());

        // when
        Long commentId = commentService.writeReply(reply.getId(), author.getId(), text);

        // then
        assertThat(commentId).isEqualTo(3L);
    }

    @Test
    @DisplayName("답글 작성시 댓글이 존재하지 않으면 예외가 발생한다")
    void writeReplyFailPost() {
        // given
        Comment comment = CommentFixture.comment();
        Member author = MemberFixture.member();
        String text = "답글 내용";
        given(commentRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> commentService.writeReply(comment.getId(), author.getId(), text))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("답글 작성시 작성자가 존재하지 않으면 예외가 발생한다")
    void writeReplyFailAuthor() {
        // given
        Comment comment = CommentFixture.comment();
        Member author = MemberFixture.member();
        String text = "댓글 내용";
        given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));
        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> commentService.writeReply(comment.getId(), author.getId(), text))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("답글을 삭제한다")
    void deleteReply() {
        // given
        Comment reply = CommentFixture.reply(CommentFixture.comment());
        given(commentRepository.findById(anyLong())).willReturn(Optional.of(reply));
        doNothing().when(commentRepository).delete(any());

        // when
        commentService.deleteComment(reply.getId(), reply.getAuthor().getId());

        // then
        then(commentRepository).should().delete(reply);
    }

}
