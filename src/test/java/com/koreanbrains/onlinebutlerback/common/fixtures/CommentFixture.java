package com.koreanbrains.onlinebutlerback.common.fixtures;

import com.koreanbrains.onlinebutlerback.entity.comment.Comment;

public class CommentFixture {
    public static Comment comment() {
        return Comment.builder()
                .id(1L)
                .text("댓글 내용")
                .post(PostFixture.post())
                .author(MemberFixture.member())
                .build();
    }
}
