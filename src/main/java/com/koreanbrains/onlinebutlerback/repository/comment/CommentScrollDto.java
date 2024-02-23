package com.koreanbrains.onlinebutlerback.repository.comment;

import java.time.LocalDateTime;

public record CommentScrollDto(Long id, String text, String author, String profile, LocalDateTime createdAt) {
}
