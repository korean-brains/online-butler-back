package com.koreanbrains.onlinebutlerback.common.security.config;

import com.koreanbrains.onlinebutlerback.common.security.dsl.RestApiDsl;
import com.koreanbrains.onlinebutlerback.common.security.entrypoint.RestAuthenticationEntryPoint;
import com.koreanbrains.onlinebutlerback.common.security.filter.RestAuthorizationFilter;
import com.koreanbrains.onlinebutlerback.common.security.handler.RestAccessDeniedHandler;
import com.koreanbrains.onlinebutlerback.common.security.jwt.JwtProvider;
import com.koreanbrains.onlinebutlerback.common.security.provider.RestAuthenticationProvider;
import com.koreanbrains.onlinebutlerback.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final RestAuthenticationProvider authenticationProvider;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    @Bean
    public SecurityFilterChain restSecurityFilterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authenticationProvider);
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http
                .securityMatcher("/api/**")
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilter(new RestAuthorizationFilter(authenticationManager, jwtProvider, memberRepository))
                .authenticationManager(authenticationManager)
                .with(new RestApiDsl<>(), restDsl -> restDsl
                        .restSuccessHandler(authenticationSuccessHandler)
                        .restFailureHandler(authenticationFailureHandler)
                        .loginProcessingUrl("/api/login")
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                        .accessDeniedHandler(new RestAccessDeniedHandler())
                )
        ;

        return http.build();
    }

}
