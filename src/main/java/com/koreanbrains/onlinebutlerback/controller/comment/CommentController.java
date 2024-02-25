package com.koreanbrains.onlinebutlerback.controller.comment;

import com.koreanbrains.onlinebutlerback.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    // TODO : Security 적용
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentWriteResponse writeComment(@RequestBody CommentWriteRequest request) {
        Long commentId = commentService.writeComment(request.postId(), 1L, request.text());
        return new CommentWriteResponse(commentId);
    }

    // TODO : Security 적용
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable("commentId") Long commentId) {
        commentService.deleteComment(commentId, 1L);
    }


}
