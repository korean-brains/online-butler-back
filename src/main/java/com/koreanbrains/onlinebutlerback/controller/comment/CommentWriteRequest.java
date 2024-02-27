package com.koreanbrains.onlinebutlerback.controller.comment;

public record CommentWriteRequest(Long postId, String text) {
}
