package com.koreanbrains.onlinebutlerback.repository.post;

import java.util.List;

public record PostScrollDto(Long id, String caption, List<String> tags) {
}
