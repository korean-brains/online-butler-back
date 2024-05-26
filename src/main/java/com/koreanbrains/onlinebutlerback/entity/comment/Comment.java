package com.koreanbrains.onlinebutlerback.entity.comment;

import com.koreanbrains.onlinebutlerback.common.entity.BaseTimeEntity;
import com.koreanbrains.onlinebutlerback.entity.member.Member;
import com.koreanbrains.onlinebutlerback.entity.post.Post;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(indexes = @Index(name = "idx_parent_id", columnList = "parent_id"))
public class Comment extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member author;

    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    private Comment parent;

    @ManyToOne(fetch = FetchType.LAZY)
    private Comment root;

}
