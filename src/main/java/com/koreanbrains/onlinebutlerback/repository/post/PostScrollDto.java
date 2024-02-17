package com.koreanbrains.onlinebutlerback.repository.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostScrollDto {

    private Long id;
    private String caption;
    private List<String> tags = new ArrayList<>();

    public void addTag(String tag) {
        this.tags.add(tag);
    }
}
