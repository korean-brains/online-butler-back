package com.koreanbrains.onlinebutlerback.common.security.controller;

import com.koreanbrains.onlinebutlerback.common.security.jwt.TokenDto;
import com.koreanbrains.onlinebutlerback.common.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody MemberLoginDto dto) {
        return ResponseEntity.ok(authService.login(dto.email(), dto.password()));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<TokenDto> reissue(@RequestBody reissueTokenRequestDto dto) {
        return ResponseEntity.ok(authService.reissueRefreshToken(dto.accessToken(), dto.refreshToken()));
    }
}
