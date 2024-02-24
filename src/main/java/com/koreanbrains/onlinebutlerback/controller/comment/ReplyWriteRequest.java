package com.koreanbrains.onlinebutlerback.controller.comment;

public record ReplyWriteRequest(Long commentId, String text) {
}
