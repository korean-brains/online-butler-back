package com.koreanbrains.onlinebutlerback.common.fixtures;

import com.koreanbrains.onlinebutlerback.entity.tag.Tag;

public class TagFixture {

    public static Tag tag() {
        return Tag.builder()
                .id(1L)
                .name("태그")
                .build();
    }
}
