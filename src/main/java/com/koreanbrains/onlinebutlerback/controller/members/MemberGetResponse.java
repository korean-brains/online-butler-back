package com.koreanbrains.onlinebutlerback.controller.members;

import com.koreanbrains.onlinebutlerback.entity.member.Member;

public record MemberGetResponse(Long id, String name, String email) {
    public MemberGetResponse(Member member){
        this(member.getId(), member.getName(), member.getEmail());
    }
}
