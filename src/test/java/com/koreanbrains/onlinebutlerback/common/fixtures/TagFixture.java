package com.koreanbrains.onlinebutlerback.common.fixtures;

import com.koreanbrains.onlinebutlerback.common.scroll.Scroll;
import com.koreanbrains.onlinebutlerback.entity.tag.Tag;
import com.koreanbrains.onlinebutlerback.repository.tag.TagScrollDto;

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

    public static Scroll<TagScrollDto> scrollTags(int size, Long nextCursor, Long nextSubCursor) {
        List<TagScrollDto> tags = new ArrayList<>();
        for (long i = 1; i <= size; i++) {
            tags.add(new TagScrollDto(i, "tag " + i, 10));
        }

        return new Scroll<>(tags, nextCursor, nextSubCursor);
    }
}
