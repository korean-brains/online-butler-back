package com.koreanbrains.onlinebutlerback.repository.member;

public record MemberDto(Long id,
                        String name,
                        String email,
                        String profileImage,
                        String introduction,
                        long postCount,
                        long followerCount,
                        long followingCount,
                        boolean isFollowed) {
}
