package com.koreanbrains.onlinebutlerback.controller.members;

import com.koreanbrains.onlinebutlerback.common.exception.EntityNotFoundException;
import com.koreanbrains.onlinebutlerback.common.exception.ErrorCode;
import com.koreanbrains.onlinebutlerback.common.security.dto.AccountDto;
import com.koreanbrains.onlinebutlerback.repository.member.MemberDto;
import com.koreanbrains.onlinebutlerback.repository.member.MemberQueryRepository;
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
    private final MemberQueryRepository memberQueryRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createMember(@RequestBody MemberCreateRequest dto){
        return memberService.createMember(dto.name(), dto.email(), dto.password());
    }

    @GetMapping("/{member-id}")
    public MemberDto getMember(@PathVariable("member-id") Long memberId){
        return memberQueryRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
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

    @PostMapping("/me/profile-image")
    @PreAuthorize("isAuthenticated()")
    public ProfileImageUpdateResponse updateProfileImage(@AuthenticationPrincipal AccountDto accountDto,
                                                         @ModelAttribute ProfileImageUpdateRequest request) {

        String profileImageUrl = memberService.updateProfileImage(accountDto.getId(), request.profileImage());
        return new ProfileImageUpdateResponse(profileImageUrl);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public MemberDto getMe(@AuthenticationPrincipal AccountDto accountDto) {
        return memberQueryRepository.findById(accountDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }

}
