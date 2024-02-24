package com.koreanbrains.onlinebutlerback.service.comment;

import com.koreanbrains.onlinebutlerback.common.exception.EntityNotFoundException;
import com.koreanbrains.onlinebutlerback.common.exception.ErrorCode;
import com.koreanbrains.onlinebutlerback.common.exception.PermissionDeniedException;
import com.koreanbrains.onlinebutlerback.entity.comment.Comment;
import com.koreanbrains.onlinebutlerback.entity.comment.ReplyComment;
import com.koreanbrains.onlinebutlerback.entity.member.Member;
import com.koreanbrains.onlinebutlerback.entity.post.Post;
import com.koreanbrains.onlinebutlerback.repository.comment.CommentRepository;
import com.koreanbrains.onlinebutlerback.repository.comment.ReplyCommentRepository;
import com.koreanbrains.onlinebutlerback.repository.member.MemberRepository;
import com.koreanbrains.onlinebutlerback.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final ReplyCommentRepository replyCommentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long writeComment(Long postId, Long authorId, String text) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.POST_NOT_FOUND));
        Member author = memberRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        Comment commentEntity = Comment.builder()
                .author(author)
                .post(post)
                .text(text)
                .build();

        return commentRepository.save(commentEntity).getId();
    }

    @Transactional
    public void deleteComment(Long commentId, Long memberId) {
        Comment findComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.COMMENT_NOT_FOUND));

        validateCommentPermission(memberId, findComment);

        replyCommentRepository.deleteByComment(findComment);
        commentRepository.delete(findComment);
    }

    @Transactional
    public Long writeReply(Long commentId, Long authorId, String text) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.COMMENT_NOT_FOUND));
        Member author = memberRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        ReplyComment reply = ReplyComment.builder()
                .author(author)
                .comment(comment)
                .text(text)
                .build();

        return replyCommentRepository.save(reply).getId();
    }

    @Transactional
    public void deleteReply(Long replyId, Long memberId) {
        ReplyComment findReply = replyCommentRepository.findById(replyId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.COMMENT_NOT_FOUND));

        validateReplyPermission(memberId, findReply);

        replyCommentRepository.delete(findReply);
    }

    private void validateCommentPermission(Long memberId, Comment comment) {
        if(!Objects.equals(comment.getAuthor().getId(), memberId)) {
            throw new PermissionDeniedException(ErrorCode.PERMISSION_DENIED);
        }
    }

    private void validateReplyPermission(Long memberId, ReplyComment comment) {
        if(!Objects.equals(comment.getAuthor().getId(), memberId)) {
            throw new PermissionDeniedException(ErrorCode.PERMISSION_DENIED);
        }
    }
}
