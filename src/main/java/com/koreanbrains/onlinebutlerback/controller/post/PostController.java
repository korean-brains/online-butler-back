package com.koreanbrains.onlinebutlerback.controller.post;

import com.koreanbrains.onlinebutlerback.common.exception.EntityNotFoundException;
import com.koreanbrains.onlinebutlerback.common.exception.ErrorCode;
import com.koreanbrains.onlinebutlerback.common.scroll.Scroll;
import com.koreanbrains.onlinebutlerback.common.security.annotation.AuthUser;
import com.koreanbrains.onlinebutlerback.common.security.dto.AccountDto;
import com.koreanbrains.onlinebutlerback.repository.comment.CommentQueryRepository;
import com.koreanbrains.onlinebutlerback.repository.comment.CommentScrollDto;
import com.koreanbrains.onlinebutlerback.repository.post.*;
import com.koreanbrains.onlinebutlerback.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostQueryRepository postQueryRepository;
    private final CommentQueryRepository commentQueryRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    public Long createPost(@AuthUser AccountDto accountDto,
                           @ModelAttribute PostCreateRequest request) {

        return postService.createPost(request.caption(), request.images(), request.tags() == null ? new String[0] : request.tags(), accountDto.getId());
    }

    @GetMapping("/{postId}")
    public PostDto getPost(@AuthUser AccountDto accountDto,
                           @PathVariable("postId") Long postId) {

        return postQueryRepository.findById(accountDto.getId(), postId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.POST_NOT_FOUND));
    }

    @PatchMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public void updatePost(@AuthUser AccountDto accountDto,
                           @PathVariable("postId") Long postId,
                           @RequestBody PostUpdateRequest request) {

        postService.updatePost(postId, request.caption(), request.tags(), accountDto.getId());
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public void deletePost(@AuthUser AccountDto accountDto,
                           @PathVariable("postId") Long postId) {

        postService.deletePost(postId, accountDto.getId());
    }

    @GetMapping
    public Scroll<PostScrollDto> scrollPost(@AuthUser AccountDto accountDto,
                                            @ModelAttribute PostScrollRequest request) {

        return postQueryRepository.scrollPost(accountDto.getId(), request.cursor(), request.tagName(), request.size());
    }

    @GetMapping("/{postId}/comment")
    public Scroll<CommentScrollDto> scrollComment(@ModelAttribute CommentScrollRequest request) {
        return commentQueryRepository.scrollComment(request.getPostId(), request.getCursor(), request.getSize());
    }

    @GetMapping("/like")
    @PreAuthorize("isAuthenticated()")
    public Scroll<PostScrollDto> scrollLikePost(@AuthUser AccountDto accountDto,
                                                @ModelAttribute LikePostScrollRequest request) {

        return postQueryRepository.scrollLikePost(accountDto.getId(), request.cursor(), accountDto.getId(), request.size());
    }

}
