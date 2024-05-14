package com.koreanbrains.onlinebutlerback.controller.follow;

import com.koreanbrains.onlinebutlerback.common.security.dto.AccountDto;
import com.koreanbrains.onlinebutlerback.service.follow.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follow")
public class FollowController {

    private final FollowService followService;

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void follow(@AuthenticationPrincipal AccountDto accountDto, @RequestBody FollowRequest request) {
        followService.follow(accountDto.getId(), request.memberId());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unFollow(@AuthenticationPrincipal AccountDto accountDto, @RequestBody UnFollowRequest request) {
        followService.unFollow(accountDto.getId(), request.memberId());
    }
}
