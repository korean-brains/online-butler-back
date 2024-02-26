package com.koreanbrains.onlinebutlerback.repository.comment;

import com.koreanbrains.onlinebutlerback.entity.comment.Comment;
import com.koreanbrains.onlinebutlerback.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    void deleteByPost(Post post);

    void deleteByRoot(Comment comment);

}
