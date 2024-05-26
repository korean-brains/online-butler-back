package com.koreanbrains.onlinebutlerback.entity.settlement;

import com.koreanbrains.onlinebutlerback.common.entity.BaseTimeEntity;
import com.koreanbrains.onlinebutlerback.entity.member.Member;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SettlementHistory extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long amount;
    @Enumerated(EnumType.STRING)
    private SettlementStatus status;

    @ManyToOne
    private Member member;
}
