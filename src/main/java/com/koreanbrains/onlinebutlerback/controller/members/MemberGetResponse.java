package com.koreanbrains.onlinebutlerback.controller.members;

public record MemberGetResponse(Long id,
                                String name,
                                String email,
                                String profileImage,
                                long postCount,
                                long followerCount,
                                long followingCount) {
}
