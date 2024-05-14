package com.koreanbrains.onlinebutlerback.repository.follow;

public record FollowDto(Long id,
                        Long memberId,
                        String name,
                        String profileImage) {
}
