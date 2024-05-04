package com.koreanbrains.onlinebutlerback.common.fixtures;

import com.koreanbrains.onlinebutlerback.entity.post.Post;

public class PostFixture {
    public static Post post() {
        return Post.builder()
                .id(1L)
                .writer(MemberFixture.member())
                .caption("포스트 내용")
                .build();
    }

    public static Post post(Long postId, Long memberId) {
        return Post.builder()
                .id(1L)
                .writer(MemberFixture.member(memberId))
                .caption("포스트 내용")
                .build();
    }
}
