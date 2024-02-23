package com.koreanbrains.onlinebutlerback.entity.comment;

import com.koreanbrains.onlinebutlerback.common.entity.BaseTimeEntity;
import com.koreanbrains.onlinebutlerback.entity.member.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReplyComment extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member author;

    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    private Comment comment;

}
