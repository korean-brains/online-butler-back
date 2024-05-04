package com.koreanbrains.onlinebutlerback.controller.post;

import com.koreanbrains.onlinebutlerback.common.exception.EntityNotFoundException;
import com.koreanbrains.onlinebutlerback.common.exception.ErrorCode;
import com.koreanbrains.onlinebutlerback.common.scroll.Scroll;
import com.koreanbrains.onlinebutlerback.common.security.dto.AccountDto;
import com.koreanbrains.onlinebutlerback.entity.post.Post;
import com.koreanbrains.onlinebutlerback.entity.post.PostImage;
import com.koreanbrains.onlinebutlerback.entity.tag.Tag;
import com.koreanbrains.onlinebutlerback.entity.tag.TagMapping;
import com.koreanbrains.onlinebutlerback.repository.comment.CommentQueryRepository;
import com.koreanbrains.onlinebutlerback.repository.comment.CommentScrollDto;
import com.koreanbrains.onlinebutlerback.repository.post.*;
import com.koreanbrains.onlinebutlerback.repository.tag.TagMappingRepository;
import com.koreanbrains.onlinebutlerback.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final TagMappingRepository tagMappingRepository;
    private final PostQueryRepository postQueryRepository;
    private final CommentQueryRepository commentQueryRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    public Long createPost(@ModelAttribute PostCreateRequest request) {
        return postService.createPost(request.caption(), request.images(), request.tags());
    }

    @GetMapping("/{postId}")
    public PostGetResponse getPost(@PathVariable("postId") Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.POST_NOT_FOUND));
        List<PostImage> postImages = postImageRepository.findByPostId(postId);
        List<Tag> tags = tagMappingRepository.findAllByPost(post).stream()
                .map(TagMapping::getTag)
                .toList();

        return new PostGetResponse(post, postImages, tags);
    }

    @PatchMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public void updatePost(@AuthenticationPrincipal AccountDto accountDto,
                           @PathVariable("postId") Long postId,
                           @RequestBody PostUpdateRequest request) {

        postService.updatePost(postId, request.caption(), request.tags(), accountDto.getId());
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public void deletePost(@AuthenticationPrincipal AccountDto accountDto,
                           @PathVariable("postId") Long postId) {

        postService.deletePost(postId, accountDto.getId());
    }

    @GetMapping
    public Scroll<PostScrollDto> scrollPost(@ModelAttribute PostScrollRequest request) {
        return postQueryRepository.scrollPost(request.cursor(), request.tagName(), request.size());
    }

    @GetMapping("/{postId}/comment")
    public Scroll<CommentScrollDto> scrollComment(@ModelAttribute CommentScrollRequest request) {
        return commentQueryRepository.scrollComment(request.getPostId(), request.getCursor(), request.getSize());
    }

    @GetMapping("/like")
    @PreAuthorize("isAuthenticated()")
    public Scroll<LikePostScrollDto> scrollLikePost(@AuthenticationPrincipal AccountDto accountDto,
                                                    @ModelAttribute LikePostScrollRequest request) {

        return postQueryRepository.scrollLikePost(request.cursor(), accountDto.getId(), request.size());
    }

}
