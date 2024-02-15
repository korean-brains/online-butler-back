package com.koreanbrains.onlinebutlerback.service.tag;

import com.koreanbrains.onlinebutlerback.common.fixtures.PostFixture;
import com.koreanbrains.onlinebutlerback.common.fixtures.TagFixture;
import com.koreanbrains.onlinebutlerback.common.fixtures.TagMappingFixture;
import com.koreanbrains.onlinebutlerback.entity.post.Post;
import com.koreanbrains.onlinebutlerback.entity.tag.Tag;
import com.koreanbrains.onlinebutlerback.entity.tag.TagMapping;
import com.koreanbrains.onlinebutlerback.repository.tag.TagMappingRepository;
import com.koreanbrains.onlinebutlerback.repository.tag.TagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @InjectMocks
    TagService tagService;

    @Mock
    TagRepository tagRepository;

    @Mock
    TagMappingRepository tagMappingRepository;

    @Test
    @DisplayName("태그와 포스트를 연결한다")
    void linkTag() {
        // given
        Post post = PostFixture.post();
        Tag tag = TagFixture.tag();
        TagMapping tagMapping = TagMappingFixture.tagMapping(post, tag);
        String[] tags = {"고양이", "뚱냥이"};

        given(tagRepository.findByName(anyString())).willReturn(Optional.of(tag));
        given(tagMappingRepository.save(any())).willReturn(tagMapping);

        // when
        tagService.linkTags(post, tags);

        // then
        then(tagRepository).should(times(2)).findByName(anyString());
        then(tagMappingRepository).should(times(2)).save(any());
    }

    @Test
    @DisplayName("태그와 포스트를 연결할 때 태그가 없으면 자동으로 생성된다")
    void linkTagCreateTag() {
        // given
        Post post = PostFixture.post();
        Tag tag = TagFixture.tag();
        TagMapping tagMapping = TagMappingFixture.tagMapping(post, tag);
        String[] tags = {"고양이", "뚱냥이"};

        given(tagRepository.findByName(anyString())).willReturn(Optional.empty());
        given(tagRepository.save(any())).willReturn(tag);
        given(tagMappingRepository.save(any())).willReturn(tagMapping);

        // when
        tagService.linkTags(post, tags);

        // then
        then(tagRepository).should(times(2)).save(any());
    }

    @Test
    @DisplayName("포스트에 연결된 태그를 초기화한다")
    void resetTags() {
        // given
        Post post = PostFixture.post();
        doNothing().when(tagMappingRepository).deleteAllByPostId(anyLong());

        // when
        tagService.resetTags(post.getId());

        // then
        then(tagMappingRepository).should().deleteAllByPostId(post.getId());
    }

}