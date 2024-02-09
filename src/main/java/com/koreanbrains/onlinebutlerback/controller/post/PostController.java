package com.koreanbrains.onlinebutlerback.controller.post;

import com.koreanbrains.onlinebutlerback.common.exception.EntityNotFoundException;
import com.koreanbrains.onlinebutlerback.common.exception.ErrorCode;
import com.koreanbrains.onlinebutlerback.entity.post.Post;
import com.koreanbrains.onlinebutlerback.entity.post.PostImage;
import com.koreanbrains.onlinebutlerback.repository.post.PostImageRepository;
import com.koreanbrains.onlinebutlerback.repository.post.PostRepository;
import com.koreanbrains.onlinebutlerback.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createPost(@RequestPart("post") PostCreateRequest request, @RequestPart("images") MultipartFile[] images) {
        return postService.createPost(request.caption(), images);
    }

    @GetMapping("/{postId}")
    public PostGetResponse getPost(@PathVariable("postId") Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.POST_NOT_FOUND));
        List<PostImage> postImages = postImageRepository.findByPostId(postId);

        return new PostGetResponse(post, postImages);
    }

    // TODO : Security 적용
    @PatchMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePost(@PathVariable("postId") Long postId,
                           @RequestBody PostUpdateRequest request) {

        postService.updatePost(postId, request.caption(), 1L);
    }

    // TODO : Security 적용
    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable("postId") Long postId) {
        postService.deletePost(postId, 1L);
    }
}
