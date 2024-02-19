package com.koreanbrains.onlinebutlerback.controller.post;

public record PostScrollRequest(Long cursor, String tagName, int size) {
}
