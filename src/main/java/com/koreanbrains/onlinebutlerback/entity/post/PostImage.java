package com.koreanbrains.onlinebutlerback.entity.post;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(indexes = @Index(name = "idx_post_id", columnList = "post_id"))
public class PostImage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    private String originalName;
    private String storedName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Post post;
}
