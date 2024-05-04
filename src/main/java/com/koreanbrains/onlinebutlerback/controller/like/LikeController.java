package com.koreanbrains.onlinebutlerback.controller.like;

import com.koreanbrains.onlinebutlerback.common.security.dto.AccountDto;
import com.koreanbrains.onlinebutlerback.service.like.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/like")
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public void likePost(@AuthenticationPrincipal AccountDto accountDto,
                         @RequestBody LikePostRequest request) {

        likeService.likePost(request.postId(), accountDto.getId());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public void dislikePost(@AuthenticationPrincipal AccountDto accountDto,
                            @RequestBody DislikePostRequest request) {

        likeService.dislikePost(request.postId(), accountDto.getId());
    }

}
