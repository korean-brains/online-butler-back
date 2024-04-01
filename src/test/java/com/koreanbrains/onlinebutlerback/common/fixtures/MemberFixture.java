package com.koreanbrains.onlinebutlerback.common.fixtures;

import com.koreanbrains.onlinebutlerback.entity.member.Member;

public class MemberFixture {
    public static Member member() {
        return Member.builder()
                .id(1L)
                .name("kim")
                .email("kim@gmail.com")
                .password("asdfasdf")
                .build();
    }

    public static Member member(Long id) {
        return Member.builder()
                .id(id)
                .name("kim")
                .email("kim@gmail.com")
                .password("asdfasdf")
                .build();
    }
}
