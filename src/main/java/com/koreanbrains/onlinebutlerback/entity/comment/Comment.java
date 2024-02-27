package com.koreanbrains.onlinebutlerback.entity.comment;

import com.koreanbrains.onlinebutlerback.common.entity.BaseTimeEntity;
import com.koreanbrains.onlinebutlerback.entity.member.Member;
import com.koreanbrains.onlinebutlerback.entity.post.Post;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(indexes = @Index(name = "idx_parent_id", columnList = "parent_id"))
public class Comment extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member author;

    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Comment parent;

    @ManyToOne(fetch = FetchType.LAZY)
    private Comment root;

}
