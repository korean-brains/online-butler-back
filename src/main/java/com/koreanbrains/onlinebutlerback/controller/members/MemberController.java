package com.koreanbrains.onlinebutlerback.controller.members;

import com.koreanbrains.onlinebutlerback.entity.member.Member;
import com.koreanbrains.onlinebutlerback.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createMember(@RequestBody MemberCreateRequest dto){
        return memberService.createMember(dto);
    }

    @GetMapping("/{member-id}")
    public MemberGetResponse getMember(@PathVariable("member-id") Long memberId){
        Member findMember = memberService.getMember(memberId);

        return new MemberGetResponse(findMember);
    }
}
