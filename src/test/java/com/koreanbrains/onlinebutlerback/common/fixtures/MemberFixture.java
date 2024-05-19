package com.koreanbrains.onlinebutlerback.common.fixtures;

import com.koreanbrains.onlinebutlerback.common.entity.UploadedFile;
import com.koreanbrains.onlinebutlerback.common.scroll.Scroll;
import com.koreanbrains.onlinebutlerback.entity.member.Member;
import com.koreanbrains.onlinebutlerback.repository.member.MemberDto;
import com.koreanbrains.onlinebutlerback.repository.member.MemberScrollDto;

import java.util.ArrayList;
import java.util.List;

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

    public static Member member(Long id, String name) {
        return Member.builder()
                .id(id)
                .name(name)
                .email(name + "@gmail.com")
                .password("asdfasdf")
                .build();
    }

    public static Member memberWithProfileImage() {
        return Member.builder()
                .id(1L)
                .name("kim")
                .email("kim@gmail.com")
                .password("asdfasdf")
                .profileImage(new UploadedFile("image.jpg", "image.jpg", "assets/image.jpg"))
                .build();
    }

    public static MemberDto memberDto() {
        return new MemberDto(1L, "kim", "kim@gmail.com", "assets/image.jpg", "hello", 10, 10, 10, false);
    }

    public static Scroll<MemberScrollDto> memberScroll(int size, Long nextCursor, Long nextSubCursor) {
        List<MemberScrollDto> members = new ArrayList<>();
        for (long i = 1; i <= size; i++) {
            members.add(new MemberScrollDto(i, "/assets/profile.jpg", "member " + i, "hello"));
        }

        return new Scroll<>(members, nextCursor, nextSubCursor);
    }

}
