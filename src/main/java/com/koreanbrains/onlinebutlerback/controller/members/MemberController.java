package com.koreanbrains.onlinebutlerback.controller.members;

import com.koreanbrains.onlinebutlerback.common.exception.EntityNotFoundException;
import com.koreanbrains.onlinebutlerback.common.exception.ErrorCode;
import com.koreanbrains.onlinebutlerback.common.scroll.Scroll;
import com.koreanbrains.onlinebutlerback.common.security.dto.AccountDto;
import com.koreanbrains.onlinebutlerback.controller.follow.FollowingListScrollRequest;
import com.koreanbrains.onlinebutlerback.controller.post.PostScrollRequest;
import com.koreanbrains.onlinebutlerback.repository.follow.FollowDto;
import com.koreanbrains.onlinebutlerback.repository.follow.FollowQueryRepository;
import com.koreanbrains.onlinebutlerback.repository.member.MemberDto;
import com.koreanbrains.onlinebutlerback.repository.member.MemberQueryRepository;
import com.koreanbrains.onlinebutlerback.repository.post.PostQueryRepository;
import com.koreanbrains.onlinebutlerback.repository.post.PostScrollDto;
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
    private final PostQueryRepository postQueryRepository;
    private final FollowQueryRepository followQueryRepository;

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

    @PostMapping("/{member-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMember(@PathVariable("member-id") Long memberId,
                             @ModelAttribute MemberUpdateRequest request){
        memberService.updateMember(memberId, request.name(), request.introduction(), request.profileImage());
    }

    @DeleteMapping("/{member-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Long disableMember(@PathVariable("member-id") Long memberId){
        return memberService.disableMember(memberId);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public MemberDto getMe(@AuthenticationPrincipal AccountDto accountDto) {
        return memberQueryRepository.findById(accountDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @GetMapping("/{memberId}/post")
    public Scroll<PostScrollDto> scrollWrittenPost(@AuthenticationPrincipal AccountDto accountDto,
                                                   @PathVariable("memberId") Long memberId,
                                                   @ModelAttribute PostScrollRequest request) {

        return postQueryRepository.scrollPost(accountDto.getId(), request.cursor(), request.size(), memberId);
    }

    @GetMapping("/{memberId}/following")
    public Scroll<FollowDto> scrollFollowingList(@PathVariable("memberId") Long memberId,
                                                 @ModelAttribute FollowingListScrollRequest request) {

        return followQueryRepository.findFollowingList(memberId, request.getCursor(), request.getSize());
    }

    @GetMapping("/{memberId}/follower")
    public Scroll<FollowDto> scrollFollowerList(@PathVariable("memberId") Long memberId,
                                                 @ModelAttribute FollowingListScrollRequest request) {

        return followQueryRepository.findFollowerList(memberId, request.getCursor(), request.getSize());
    }

}
