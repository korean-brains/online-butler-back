package com.koreanbrains.onlinebutlerback.common.security.repository;

import com.koreanbrains.onlinebutlerback.entity.token.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    boolean existsByParentToken(String parentToken);
    Optional<RefreshToken> findByToken(String token);

    void deleteByTokenGroup(String tokenGroup);

    void deleteByExpirationBefore(LocalDateTime dateTime);
}
