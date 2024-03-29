package com.koreanbrains.onlinebutlerback.repository.tag;

import com.koreanbrains.onlinebutlerback.entity.post.Post;
import com.koreanbrains.onlinebutlerback.entity.tag.TagMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagMappingRepository extends JpaRepository<TagMapping, Long> {

    @Modifying
    @Query("delete from TagMapping tm where tm.post.id = :postId")
    void deleteAllByPostId(@Param("postId") Long postId);

    List<TagMapping> findAllByPost(Post post);
}
