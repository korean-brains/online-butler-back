package com.koreanbrains.onlinebutlerback.controller.comment;

import com.koreanbrains.onlinebutlerback.service.comment.ReplyCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reply")
public class ReplyCommentController {

    private final ReplyCommentService replyCommentService;

    // TODO : Security 적용
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long writeReply(@RequestBody ReplyWriteRequest request) {
        return replyCommentService.writeReply(request.commentId(), 1L, request.text());
    }

    // TODO : Security 적용
    @DeleteMapping("/{replyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReply(@PathVariable("replyId") Long replyId) {
        replyCommentService.deleteReply(replyId, 1L);
    }
}
