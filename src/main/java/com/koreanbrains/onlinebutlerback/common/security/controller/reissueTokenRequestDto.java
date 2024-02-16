package com.koreanbrains.onlinebutlerback.common.security.controller;

public record reissueTokenRequestDto(String accessToken, String refreshToken) {
}
