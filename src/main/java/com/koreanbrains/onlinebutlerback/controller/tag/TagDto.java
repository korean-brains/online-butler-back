package com.koreanbrains.onlinebutlerback.controller.tag;

import com.koreanbrains.onlinebutlerback.entity.tag.Tag;

public record TagDto(Long id, String name) {
    public TagDto(Tag tag) {
        this(tag.getId(), tag.getName());
    }
}
