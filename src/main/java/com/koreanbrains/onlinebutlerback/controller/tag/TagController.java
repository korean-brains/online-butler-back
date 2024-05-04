package com.koreanbrains.onlinebutlerback.controller.tag;

import com.koreanbrains.onlinebutlerback.common.scroll.Scroll;
import com.koreanbrains.onlinebutlerback.entity.tag.Tag;
import com.koreanbrains.onlinebutlerback.repository.tag.TagQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tag")
public class TagController {

    private final TagQueryRepository tagQueryRepository;

    @GetMapping
    public Scroll<TagDto> searchTag(@ModelAttribute TagSearchRequest request) {
        Scroll<Tag> tags = tagQueryRepository.searchTag(request.cursor(), request.tag(), request.size());
        return tags.map(TagDto::new);
    }
}
