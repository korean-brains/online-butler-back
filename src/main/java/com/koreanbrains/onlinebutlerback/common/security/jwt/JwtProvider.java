package com.koreanbrains.onlinebutlerback.common.security.jwt;

import com.koreanbrains.onlinebutlerback.common.exception.ErrorCode;
import com.koreanbrains.onlinebutlerback.common.exception.PermissionDeniedException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtProvider {

    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;

    public JwtProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public TokenDto generateTokens(Long memberId) {
        return new TokenDto(generateAccessToken(memberId), generateRefreshToken());
    }

    public String generateAccessToken(Long memberId) {
        Date expireDate = new Date(new Date().getTime() + jwtProperties.getAccessTokenExpireTime());

        return Jwts.builder()
                .issuer("online-butler")
                .issuedAt(new Date())
                .expiration(expireDate)
                .subject("AccessToken")
                .claim("memberId", memberId)
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken() {
        Date expireDate = new Date(new Date().getTime() + jwtProperties.getRefreshTokenExpireTime());

        return Jwts.builder()
                .issuer("online-butler")
                .issuedAt(new Date())
                .expiration(expireDate)
                .subject("RefreshToken")
                .signWith(secretKey)
                .compact();
    }

    public long getMemberIdFromAccessToken(String token) {
        validateToken(token);
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)
                .getPayload()
                .get("memberId", Long.class);
    }

    public Date getExpiration(String token) {
        validateToken(token);
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)
                .getPayload().getExpiration();
    }

    public void validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new PermissionDeniedException(ErrorCode.INVALID_SIGNATURE);
        } catch (ExpiredJwtException e) {
            throw new PermissionDeniedException(ErrorCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new PermissionDeniedException(ErrorCode.UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new PermissionDeniedException(ErrorCode.INVALID_TOKEN);
        }
    }

}
