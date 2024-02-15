package com.koreanbrains.onlinebutlerback.common.security.jwt;

import java.util.Date;

public record TokenDto (String grantType, String accessToken, long accessTokenExpiresIn, String refreshToken) {
}
