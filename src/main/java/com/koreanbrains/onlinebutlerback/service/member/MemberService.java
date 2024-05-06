package com.koreanbrains.onlinebutlerback.service.member;

import com.koreanbrains.onlinebutlerback.common.entity.UploadedFile;
import com.koreanbrains.onlinebutlerback.common.exception.EntityNotFoundException;
import com.koreanbrains.onlinebutlerback.common.exception.ErrorCode;
import com.koreanbrains.onlinebutlerback.common.util.file.FileStore;
import com.koreanbrains.onlinebutlerback.common.util.file.UploadFile;
import com.koreanbrains.onlinebutlerback.entity.member.Member;
import com.koreanbrains.onlinebutlerback.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final FileStore fileStore;

    @Transactional
    public Long createMember(String name, String email, String password) {
        Member member = Member.builder()
                .name(name)
                .email(email)
                .password(password)
                .build();

        return memberRepository.save(member).getId();
    }

    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Transactional
    public Long updateMember(Long memberId, String name) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        findMember.updateName(name);

        return findMember.getId();
    }

    @Transactional
    public Long disableMember(Long memberId) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        findMember.disableMember();

        return findMember.getId();
    }

    @Transactional
    public String updateProfileImage(Long memberId, MultipartFile profileImage) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        if (member.getProfileImage() != null) {
            deleteOldProfileImage(member);
        }

        UploadFile uploadedImage = fileStore.upload(profileImage, UUID.randomUUID().toString());
        member.updateProfileImage(new UploadedFile(uploadedImage));

        return uploadedImage.url();
    }

    private void deleteOldProfileImage(Member member) {
        fileStore.delete(member.getProfileImage().getStoreFilename());
    }
}
