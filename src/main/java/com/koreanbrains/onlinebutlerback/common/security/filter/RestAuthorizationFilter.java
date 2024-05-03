package com.koreanbrains.onlinebutlerback.common.security.filter;

import com.koreanbrains.onlinebutlerback.common.dto.AccountContext;
import com.koreanbrains.onlinebutlerback.common.dto.AccountDto;
import com.koreanbrains.onlinebutlerback.common.exception.BaseException;
import com.koreanbrains.onlinebutlerback.common.exception.EntityNotFoundException;
import com.koreanbrains.onlinebutlerback.common.exception.ErrorCode;
import com.koreanbrains.onlinebutlerback.common.exception.PermissionDeniedException;
import com.koreanbrains.onlinebutlerback.common.security.jwt.JwtProvider;
import com.koreanbrains.onlinebutlerback.common.security.token.RestAuthenticationToken;
import com.koreanbrains.onlinebutlerback.entity.member.Member;
import com.koreanbrains.onlinebutlerback.repository.member.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class RestAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    public RestAuthorizationFilter(AuthenticationManager authenticationManager,
                                   JwtProvider jwtProvider,
                                   MemberRepository memberRepository) {

        super(authenticationManager);
        this.jwtProvider = jwtProvider;
        this.memberRepository = memberRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            String accessToken = getAccessTokenFromHeader(request);

            long memberId = jwtProvider.getMemberIdFromAccessToken(accessToken);
            Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

            AccountDto accountDto = com.koreanbrains.onlinebutlerback.common.dto.AccountDto.builder()
                    .id(member.getId())
                    .name(member.getName())
                    .email(member.getEmail())
                    .password(member.getPassword())
                    .role(member.getRole())
                    .build();

            AccountContext accountContext = new AccountContext(accountDto);

            RestAuthenticationToken restAuthenticationToken = new RestAuthenticationToken(accountContext.getAccountDto(), null, accountContext.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(restAuthenticationToken);
        } catch (BaseException e) {
            request.setAttribute("exception", e.getErrorCode().name());
        } catch (Exception e) {
            request.setAttribute("exception", ErrorCode.UN_AUTHORIZE.name());
        }

        chain.doFilter(request, response);
    }

    private String getAccessTokenFromHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith("Bearer")) {
            throw new PermissionDeniedException(ErrorCode.PERMISSION_DENIED);
        }

        return authorizationHeader.substring(7);
    }
}
