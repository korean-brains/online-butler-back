package com.koreanbrains.onlinebutlerback.service.tag;


import com.koreanbrains.onlinebutlerback.entity.post.Post;
import com.koreanbrains.onlinebutlerback.entity.tag.Tag;
import com.koreanbrains.onlinebutlerback.entity.tag.TagMapping;
import com.koreanbrains.onlinebutlerback.repository.tag.TagMappingRepository;
import com.koreanbrains.onlinebutlerback.repository.tag.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {

    private final TagRepository tagRepository;
    private final TagMappingRepository tagMappingRepository;

    @Transactional
    public void linkTags(Post post, String[] tagNames) {
        for (String tagName : tagNames) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> tagRepository.save(Tag.builder().name(tagName).build()));

            tagMappingRepository.save(TagMapping.builder()
                    .tag(tag)
                    .post(post)
                    .build());
        }
    }

    @Transactional
    public void resetTags(Long postId) {
        tagMappingRepository.deleteAllByPostId(postId);
    }
}
