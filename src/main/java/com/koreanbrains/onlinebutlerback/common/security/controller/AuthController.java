package com.koreanbrains.onlinebutlerback.common.security.controller;

import com.koreanbrains.onlinebutlerback.common.security.jwt.TokenDto;
import com.koreanbrains.onlinebutlerback.common.security.service.RefreshTokenService;
import com.koreanbrains.onlinebutlerback.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/api/signup")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void signup(@RequestBody SignupRequest request) {
        memberService.createMember(request.name(),
                request.email(),
                passwordEncoder.encode(request.password()));
    }

    @PostMapping("/api/reissue-token")
    public TokenDto reissueToken(@RequestBody ReissueTokenRequest request) {
        return refreshTokenService.reissue(request.refreshToken());
    }

    @PostMapping("/api/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestBody LogoutRequest request) {
        refreshTokenService.deleteRefreshTokenGroup(request.refreshToken());
    }

}
