package com.koreanbrains.onlinebutlerback.repository.member;

public record MemberDto(Long id,
                        String name,
                        String email,
                        String profileImage,
                        long postCount,
                        long followerCount,
                        long followingCount) {
}
