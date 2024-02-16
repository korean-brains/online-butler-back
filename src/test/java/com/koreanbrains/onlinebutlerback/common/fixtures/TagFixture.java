package com.koreanbrains.onlinebutlerback.common.fixtures;

import com.koreanbrains.onlinebutlerback.entity.tag.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagFixture {

    public static Tag tag() {
        return Tag.builder()
                .id(1L)
                .name("태그")
                .build();
    }

    public static List<Tag> tags(String... names) {
        List<Tag> tags = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            tags.add(Tag.builder()
                    .id((long) i + 1)
                    .name(names[i])
                    .build());
        }

        return tags;
    }
}
