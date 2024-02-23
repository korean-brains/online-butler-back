package com.koreanbrains.onlinebutlerback.controller.post;

import com.koreanbrains.onlinebutlerback.common.scroll.ScrollRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class CommentScrollRequest extends ScrollRequest<Long, Long> {
    private Long postId;
}
