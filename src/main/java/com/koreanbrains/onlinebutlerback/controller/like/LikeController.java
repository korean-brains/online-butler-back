package com.koreanbrains.onlinebutlerback.controller.like;

import com.koreanbrains.onlinebutlerback.service.like.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/like")
public class LikeController {

    private final LikeService likeService;

    // TODO : Security 적용
    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void likePost(@RequestBody LikePostRequest request) {
        likeService.likePost(request.postId(), 1L);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void dislikePost(@RequestBody DislikePostRequest request) {
        likeService.dislikePost(request.postId(), 1L);
    }

}
