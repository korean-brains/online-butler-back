package com.koreanbrains.onlinebutlerback.controller.members;

import com.koreanbrains.onlinebutlerback.common.security.dto.AccountDto;
import com.koreanbrains.onlinebutlerback.entity.member.Member;
import com.koreanbrains.onlinebutlerback.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createMember(@RequestBody MemberCreateRequest dto){
        return memberService.createMember(dto.name(), dto.email(), dto.password());
    }

    @GetMapping("/{member-id}")
    public MemberGetResponse getMember(@PathVariable("member-id") Long memberId){
        Member findMember = memberService.getMember(memberId);

        return new MemberGetResponse(findMember);
    }

    @PatchMapping("/{member-id}")
    @ResponseStatus(HttpStatus.OK)
    public Long updateMember(@PathVariable("member-id") Long memberId,
                             @RequestBody MemberUpdateRequest dto){
        return memberService.updateMember(memberId, dto.name());
    }

    @DeleteMapping("/{member-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Long disableMember(@PathVariable("member-id") Long memberId){
        return memberService.disableMember(memberId);
    }

    @PutMapping("/me/profile-image")
    @PreAuthorize("isAuthenticated()")
    public ProfileImageUpdateResponse updateProfileImage(@AuthenticationPrincipal AccountDto accountDto,
                                                         @ModelAttribute ProfileImageUpdateRequest request) {

        String profileImageUrl = memberService.updateProfileImage(accountDto.getId(), request.profileImage());
        return new ProfileImageUpdateResponse(profileImageUrl);
    }

}
