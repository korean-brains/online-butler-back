package com.koreanbrains.onlinebutlerback.common.fixtures;

import com.koreanbrains.onlinebutlerback.entity.like.Like;

public class LikeFixture {

    public static Like like() {
        return Like.builder()
                .id(1L)
                .post(PostFixture.post())
                .member(MemberFixture.member())
                .build();
    }
}
