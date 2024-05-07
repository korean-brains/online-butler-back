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
public class PostDto {

    private Long id;
    private String caption;
    private LocalDateTime createdAt;
    private long likeCount;
    private long commentCount;
    private Writer writer;

    private List<String> tags = new ArrayList<>();
    private List<String> images = new ArrayList<>();

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Writer {
        private Long id;
        private String name;
        private String profile;
        private boolean followed;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
