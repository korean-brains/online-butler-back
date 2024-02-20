package com.koreanbrains.onlinebutlerback.controller.post;

import org.springframework.web.multipart.MultipartFile;

public record PostCreateRequest(String caption, String[] tags, MultipartFile[] images) {
}
