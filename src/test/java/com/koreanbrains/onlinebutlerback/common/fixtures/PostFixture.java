package com.koreanbrains.onlinebutlerback.common.fixtures;

import com.koreanbrains.onlinebutlerback.entity.post.Post;

public class PostFixture {
    public static Post post() {
        return Post.builder()
                .id(1L)
                .memberId(1L)
                .caption("포스트 내용")
                .build();
    }
}
