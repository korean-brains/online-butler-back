package com.koreanbrains.onlinebutlerback.service.like;

import com.koreanbrains.onlinebutlerback.common.exception.EntityExistsException;
import com.koreanbrains.onlinebutlerback.common.exception.EntityNotFoundException;
import com.koreanbrains.onlinebutlerback.common.exception.ErrorCode;
import com.koreanbrains.onlinebutlerback.entity.like.Like;
import com.koreanbrains.onlinebutlerback.entity.member.Member;
import com.koreanbrains.onlinebutlerback.entity.post.Post;
import com.koreanbrains.onlinebutlerback.repository.like.LikeRepository;
import com.koreanbrains.onlinebutlerback.repository.member.MemberRepository;
import com.koreanbrains.onlinebutlerback.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void likePost(Long postId, Long memberId) {
        likeRepository.findByPostIdAndMemberId(postId, memberId)
                .ifPresent((like) -> { throw new EntityExistsException(ErrorCode.ALREADY_LIKE_POST); });

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.POST_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        Like like = Like.builder()
                .post(post)
                .member(member)
                .build();

        likeRepository.save(like);
    }

    @Transactional
    public void dislikePost(Long postId, Long memberId) {
        Like like = likeRepository.findByPostIdAndMemberId(postId, memberId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.LIKE_NOT_FOUND));

        likeRepository.delete(like);
    }
}
