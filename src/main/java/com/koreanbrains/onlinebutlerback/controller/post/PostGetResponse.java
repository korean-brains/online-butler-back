package com.koreanbrains.onlinebutlerback.controller.post;

import com.koreanbrains.onlinebutlerback.entity.post.Post;
import com.koreanbrains.onlinebutlerback.entity.post.PostImage;
import com.koreanbrains.onlinebutlerback.entity.tag.Tag;

import java.util.List;

public record PostGetResponse(Long id, String caption, List<String> images, List<String> tags) {

    public PostGetResponse(Post post, List<PostImage> postImages, List<Tag> tags) {
        this(post.getId(),
                post.getCaption(),
                postImages.stream()
                        .map(PostImage::getUrl)
                        .toList(),
                tags.stream()
                        .map(Tag::getName)
                        .toList()
        );
    }

}
