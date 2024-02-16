package com.koreanbrains.onlinebutlerback.common.security.service;

import com.koreanbrains.onlinebutlerback.common.exception.ErrorCode;
import com.koreanbrains.onlinebutlerback.common.exception.PermissionDeniedException;
import com.koreanbrains.onlinebutlerback.common.security.controller.MemberLoginDto;
import com.koreanbrains.onlinebutlerback.common.security.controller.reissueTokenRequestDto;
import com.koreanbrains.onlinebutlerback.common.security.entity.RefreshToken;
import com.koreanbrains.onlinebutlerback.common.security.jwt.JwtTokenProvider;
import com.koreanbrains.onlinebutlerback.common.security.jwt.TokenDto;
import com.koreanbrains.onlinebutlerback.common.security.repository.RefreshTokenRepository;
import com.koreanbrains.onlinebutlerback.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenDto login(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = toAuthentication(email, password);

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(authentication);

        refreshTokenRepository.save(RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.refreshToken())
                .build());

        return tokenDto;
    }

    public TokenDto reissueRefreshToken(String accessToken, String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new PermissionDeniedException(ErrorCode.UNSUPPORTED_TOKEN);
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        RefreshToken findRefreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new PermissionDeniedException(ErrorCode.EXPIRED_TOKEN));

        if (!findRefreshToken.getValue().equals(refreshToken)) {
            throw new PermissionDeniedException(ErrorCode.INVALID_TOKEN);
        }

        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(authentication);

        RefreshToken newRefreshToken = findRefreshToken.updateValue(tokenDto.refreshToken());
        refreshTokenRepository.save(newRefreshToken);

        return tokenDto;
    }

    private UsernamePasswordAuthenticationToken toAuthentication(String email, String password){
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}
