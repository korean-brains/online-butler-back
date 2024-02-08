package com.koreanbrains.onlinebutlerback.controller.post;

import com.koreanbrains.onlinebutlerback.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createPost(@RequestPart("post") PostCreateRequest request, @RequestPart("images") MultipartFile[] images) {
        return postService.createPost(request.caption(), images);
    }

}
