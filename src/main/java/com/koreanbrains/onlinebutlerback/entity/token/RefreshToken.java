package com.koreanbrains.onlinebutlerback.entity.token;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RefreshToken {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;
    private String parentToken;
    private LocalDateTime expiration;
    private String tokenGroup;
    private Long memberId;

}
