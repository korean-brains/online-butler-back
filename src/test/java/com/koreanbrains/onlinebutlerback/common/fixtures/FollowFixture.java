package com.koreanbrains.onlinebutlerback.common.fixtures;

import com.koreanbrains.onlinebutlerback.entity.follow.Follow;
import com.koreanbrains.onlinebutlerback.entity.member.Member;

public class FollowFixture {

    public static Follow follow() {
        return Follow.builder()
                .id(1L)
                .follower(MemberFixture.member(1L))
                .following(MemberFixture.member(2L))
                .build();
    }

    public static Follow follow(Member follower, Member following) {
        return Follow.builder()
                .id(1L)
                .follower(follower)
                .following(following)
                .build();
    }
}
