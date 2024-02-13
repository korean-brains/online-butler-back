package com.koreanbrains.onlinebutlerback.common.fixtures;

import com.koreanbrains.onlinebutlerback.entity.post.Post;
import com.koreanbrains.onlinebutlerback.entity.post.PostImage;

public class PostImageFixture {
    public static PostImage postImage(Long id, Post post) {
        return PostImage.builder()
                .id(id)
                .post(post)
                .originalName("image.jpg")
                .storedName("9257a629-f0a3-4fc4-8264-77baf3092644.jpg")
                .url("https://online-butler-s3.s3.ap-northeast-2.amazonaws.com/9257a629-f0a3-4fc4-8264-77baf3092644.jpg")
                .build();
    }
}
