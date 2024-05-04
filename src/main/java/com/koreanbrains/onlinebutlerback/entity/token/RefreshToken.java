package com.koreanbrains.onlinebutlerback.entity.token;

import com.koreanbrains.onlinebutlerback.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RefreshToken extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;
    private String parentToken;
    private LocalDateTime expiration;
    private String tokenGroup;
    private Long memberId;

}
