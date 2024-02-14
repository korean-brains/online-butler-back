package com.koreanbrains.onlinebutlerback.entity.post;

import com.koreanbrains.onlinebutlerback.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String caption;

    @CreatedBy
    @Column(nullable = false, updatable = false)
    private Long memberId;

    public void changeCaption(String caption) {
        this.caption = caption;
    }
}
