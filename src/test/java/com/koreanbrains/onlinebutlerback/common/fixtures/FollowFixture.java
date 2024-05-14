package com.koreanbrains.onlinebutlerback.common.fixtures;

import com.koreanbrains.onlinebutlerback.entity.follow.Follow;
import com.koreanbrains.onlinebutlerback.entity.member.Member;
import com.koreanbrains.onlinebutlerback.repository.follow.FollowDto;

import java.util.ArrayList;
import java.util.List;

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

    public static Follow follow(Long id, Member follower, Member following) {
        return Follow.builder()
                .id(id)
                .follower(follower)
                .following(following)
                .build();
    }

    public static List<FollowDto> followList(long startId, long endId) {
        List<FollowDto> follows = new ArrayList<>();

        long id = startId;
        for (long i = 0; i <= Math.abs(startId - endId); i++) {
            FollowDto followDto = new FollowDto(
                    id,
                    i + 1L,
                    "member" + (i + 1),
                    "assets/profile.jpg"
            );
            follows.add(followDto);
            id += startId < endId ? 1 : -1;
        }

        return follows;
    }
}
