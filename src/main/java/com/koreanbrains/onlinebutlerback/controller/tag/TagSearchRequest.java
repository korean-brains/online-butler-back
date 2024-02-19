package com.koreanbrains.onlinebutlerback.controller.tag;

public record TagSearchRequest(Long cursor, String tag, int size) {
}
