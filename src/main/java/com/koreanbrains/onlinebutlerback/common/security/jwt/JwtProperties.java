package com.koreanbrains.onlinebutlerback.common.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties("auth.jwt")
public class JwtProperties {
    private String secret;
    private long accessTokenExpireTime;
    private long refreshTokenExpireTime;
}
