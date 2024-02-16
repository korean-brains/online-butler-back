package com.koreanbrains.onlinebutlerback.common.security.repository;

import com.koreanbrains.onlinebutlerback.common.security.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByKey(String key);
}
