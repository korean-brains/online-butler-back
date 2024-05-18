package com.koreanbrains.onlinebutlerback.repository.comment;

import java.time.LocalDateTime;

public record ReplyScrollDto(Long id,
                             String text,
                             String author,
                             String profile,
                             String parentAuthor,
                             String parentProfile,
                             LocalDateTime createdAt,
                             Long rootCommentId) {
}
