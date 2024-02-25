package com.koreanbrains.onlinebutlerback.service.comment;

import com.koreanbrains.onlinebutlerback.common.exception.EntityNotFoundException;
import com.koreanbrains.onlinebutlerback.common.exception.ErrorCode;
import com.koreanbrains.onlinebutlerback.common.exception.PermissionDeniedException;
import com.koreanbrains.onlinebutlerback.entity.comment.Comment;
import com.koreanbrains.onlinebutlerback.entity.comment.ReplyComment;
import com.koreanbrains.onlinebutlerback.entity.member.Member;
import com.koreanbrains.onlinebutlerback.repository.comment.CommentRepository;
import com.koreanbrains.onlinebutlerback.repository.comment.ReplyCommentRepository;
import com.koreanbrains.onlinebutlerback.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReplyCommentService {

    private final ReplyCommentRepository replyCommentRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

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

    private void validateReplyPermission(Long memberId, ReplyComment comment) {
        if(!Objects.equals(comment.getAuthor().getId(), memberId)) {
            throw new PermissionDeniedException(ErrorCode.PERMISSION_DENIED);
        }
    }
}
