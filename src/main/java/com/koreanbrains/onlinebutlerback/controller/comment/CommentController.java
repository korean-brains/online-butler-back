package com.koreanbrains.onlinebutlerback.controller.comment;

import com.koreanbrains.onlinebutlerback.common.scroll.Scroll;
import com.koreanbrains.onlinebutlerback.common.security.annotation.AuthUser;
import com.koreanbrains.onlinebutlerback.common.security.dto.AccountDto;
import com.koreanbrains.onlinebutlerback.repository.comment.CommentQueryRepository;
import com.koreanbrains.onlinebutlerback.repository.comment.ReplyScrollDto;
import com.koreanbrains.onlinebutlerback.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;
    private final CommentQueryRepository commentQueryRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    public CommentWriteResponse writeComment(@AuthUser AccountDto accountDto,
                                             @RequestBody CommentWriteRequest request) {

        Long commentId = commentService.writeComment(request.postId(), accountDto.getId(), request.text());
        return new CommentWriteResponse(commentId);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public void deleteComment(@AuthUser AccountDto accountDto,
                              @PathVariable("commentId") Long commentId) {

        commentService.deleteComment(commentId, accountDto.getId());
    }

    @PostMapping("/{commentId}/reply")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    public ReplyWriteResponse writeReply(@AuthUser AccountDto accountDto,
                                         @PathVariable("commentId") Long commentId,
                                         @RequestBody ReplyWriteRequest request) {

        Long replyId = commentService.writeReply(commentId, accountDto.getId(), request.text());
        return new ReplyWriteResponse(replyId);
    }

    @GetMapping("/{commentId}/reply")
    public Scroll<ReplyScrollDto> scrollReply(@PathVariable("commentId") Long commentId, @ModelAttribute ReplyScrollRequest request) {
        return commentQueryRepository.scrollReply(commentId, request.cursor(), request.size());
    }


}
