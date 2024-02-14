package com.koreanbrains.onlinebutlerback.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
@RequiredArgsConstructor
public class JpaAuditingConfig  {

    @Bean
    public AuditorAware<Long> auditorAware() {
//        return () -> {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            if (authentication == null || !authentication.isAuthenticated())
//                return Optional.empty();
//
//            return Optional.of((Long) authentication.getPrincipal());
//        };
        return () -> Optional.of(1L);
    }
}
