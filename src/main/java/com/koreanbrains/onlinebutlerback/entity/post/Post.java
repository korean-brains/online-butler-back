package com.koreanbrains.onlinebutlerback.entity.post;

import com.koreanbrains.onlinebutlerback.common.entity.BaseTimeEntity;
import com.koreanbrains.onlinebutlerback.entity.member.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String caption;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;

    public void changeCaption(String caption) {
        this.caption = caption;
    }
}
