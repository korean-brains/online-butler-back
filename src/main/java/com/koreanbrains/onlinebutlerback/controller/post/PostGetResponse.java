package com.koreanbrains.onlinebutlerback.controller.post;

import com.koreanbrains.onlinebutlerback.entity.post.Post;
import com.koreanbrains.onlinebutlerback.entity.post.PostImage;

import java.util.List;

public record PostGetResponse(Long id, String caption, List<String> images) {

    public PostGetResponse(Post post, List<PostImage> postImages) {
        this(post.getId(), post.getCaption(), postImages.stream()
                .map(PostImage::getUrl)
                .toList());
    }

}
