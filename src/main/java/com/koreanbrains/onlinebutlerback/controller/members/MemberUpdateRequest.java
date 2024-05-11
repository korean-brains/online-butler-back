package com.koreanbrains.onlinebutlerback.controller.members;

import org.springframework.web.multipart.MultipartFile;

public record MemberUpdateRequest(String name, String introduction, MultipartFile profileImage) {
}
