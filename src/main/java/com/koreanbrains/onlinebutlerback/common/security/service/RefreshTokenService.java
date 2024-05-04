package com.koreanbrains.onlinebutlerback.common.security.service;

import com.koreanbrains.onlinebutlerback.common.exception.AuthenticationException;
import com.koreanbrains.onlinebutlerback.common.exception.EntityNotFoundException;
import com.koreanbrains.onlinebutlerback.common.exception.ErrorCode;
import com.koreanbrains.onlinebutlerback.common.security.jwt.JwtProvider;
import com.koreanbrains.onlinebutlerback.common.security.jwt.TokenDto;
import com.koreanbrains.onlinebutlerback.common.security.repository.RefreshTokenRepository;
import com.koreanbrains.onlinebutlerback.entity.member.Member;
import com.koreanbrains.onlinebutlerback.entity.token.RefreshToken;
import com.koreanbrains.onlinebutlerback.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public void save(String refreshToken, Long memberId) {
        LocalDateTime expiration = jwtProvider.getExpiration(refreshToken).toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        RefreshToken token = RefreshToken.builder()
                .token(refreshToken)
                .expiration(expiration)
                .memberId(member.getId())
                .tokenGroup(UUID.randomUUID().toString())
                .build();

        refreshTokenRepository.save(token);
    }

    @Transactional(noRollbackFor = AuthenticationException.class)
    public TokenDto reissue(String refreshToken) {
        jwtProvider.validateToken(refreshToken);

        RefreshToken findRefreshToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new AuthenticationException(ErrorCode.INVALID_REFRESH_TOKEN));

        if(refreshTokenRepository.existsByParentToken(refreshToken)) {
            refreshTokenRepository.deleteByTokenGroup(findRefreshToken.getTokenGroup());
            throw new AuthenticationException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Member member = memberRepository.findById(findRefreshToken.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        TokenDto tokenDto = jwtProvider.generateTokens(member.getId());
        LocalDateTime expiration = jwtProvider.getExpiration(tokenDto.refreshToken()).toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        RefreshToken token = RefreshToken.builder()
                .parentToken(findRefreshToken.getToken())
                .token(tokenDto.refreshToken())
                .expiration(expiration)
                .memberId(findRefreshToken.getMemberId())
                .tokenGroup(findRefreshToken.getTokenGroup())
                .build();

        refreshTokenRepository.save(token);

        return tokenDto;
    }

    @Transactional
    public void deleteRefreshTokenGroup(String refreshToken) {
        RefreshToken findRefreshToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new AuthenticationException(ErrorCode.INVALID_REFRESH_TOKEN));

        refreshTokenRepository.deleteByTokenGroup(findRefreshToken.getTokenGroup());
    }

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정
    @Transactional
    public void deleteExpiredTokens() {
        refreshTokenRepository.deleteByExpirationBefore(LocalDateTime.now());
    }
}
