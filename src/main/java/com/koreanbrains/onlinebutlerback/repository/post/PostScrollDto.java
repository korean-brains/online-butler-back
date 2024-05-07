package com.koreanbrains.onlinebutlerback.repository.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostScrollDto {

    private Long id;
    private String caption;
    private LocalDateTime createdAt;
    private long likeCount;
    private long commentCount;
    private Writer writer;

    private List<String> tags = new ArrayList<>();
    private List<String> images = new ArrayList<>();

    public PostScrollDto(Long id, String caption, LocalDateTime createdAt, long likeCount, long commentCount, Writer writer) {
        this.id = id;
        this.caption = caption;
        this.createdAt = createdAt;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.writer = writer;
    }

    public void addTag(String tag) {
        this.tags.add(tag);
    }
    public void addImage(String image) {
        this.images.add(image);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Writer {
        private Long id;
        private String name;
        private String profile;
        private boolean followed;
    }
}
