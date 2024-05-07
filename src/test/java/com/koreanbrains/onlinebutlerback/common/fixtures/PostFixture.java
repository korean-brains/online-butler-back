package com.koreanbrains.onlinebutlerback.common.fixtures;

import com.koreanbrains.onlinebutlerback.entity.post.Post;
import com.koreanbrains.onlinebutlerback.repository.post.PostScrollDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostFixture {
    public static Post post() {
        return Post.builder()
                .id(1L)
                .writer(MemberFixture.member())
                .caption("포스트 내용")
                .build();
    }

    public static Post post(Long postId, Long memberId) {
        return Post.builder()
                .id(1L)
                .writer(MemberFixture.member(memberId))
                .caption("포스트 내용")
                .build();
    }

    public static List<PostScrollDto> scrollPost(long startId, long endId) {
        List<PostScrollDto> posts = new ArrayList<>();

        long id = startId;
        for (long i = 0; i <= Math.abs(startId - endId); i++) {
            PostScrollDto postScrollDto = new PostScrollDto(
                    id,
                    "포스트 내용",
                    LocalDateTime.of(2024, 4, 1, 12, 0, 0),
                    0,
                    0,
                    new PostScrollDto.Writer(1L, "홍길동", "assets/image.jpg", false),
                    List.of("태그1", "태그2"),
                    List.of("assets/image1.jpg", "assets/image2.jpg")
            );

            posts.add(postScrollDto);
            id += startId < endId ? 1 : -1;
        }

        return posts;
    }
}
