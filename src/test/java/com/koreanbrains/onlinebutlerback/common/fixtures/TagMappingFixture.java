package com.koreanbrains.onlinebutlerback.common.fixtures;

import com.koreanbrains.onlinebutlerback.entity.post.Post;
import com.koreanbrains.onlinebutlerback.entity.tag.Tag;
import com.koreanbrains.onlinebutlerback.entity.tag.TagMapping;

public class TagMappingFixture {

    public static TagMapping tagMapping(Post post, Tag tag) {
        return TagMapping.builder()
                .id(1L)
                .post(post)
                .tag(tag)
                .build();
    }
}
