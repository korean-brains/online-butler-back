package com.koreanbrains.onlinebutlerback.repository.like;

import com.koreanbrains.onlinebutlerback.entity.like.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    @Query("select l from Like l where l.post.id = :postId and l.member.id = :memberId")
    Optional<Like> findByPostIdAndMemberId(@Param("postId") Long postId, @Param("memberId") Long memberId);
}
