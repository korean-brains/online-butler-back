package com.koreanbrains.onlinebutlerback.repository.post;

import com.koreanbrains.onlinebutlerback.entity.post.Post;
import com.koreanbrains.onlinebutlerback.entity.post.PostImage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class PostImageRepositoryTest {

    @Autowired
    PostImageRepository postImageRepository;

    @Test
    @DisplayName("포스트에 등록된 이미지를 조회한다")
    void findByPostId() {
        // given
        Post post = Post.builder().id(1L).build();
        postImageRepository.save(PostImage.builder().post(post).build());
        postImageRepository.save(PostImage.builder().post(post).build());
        postImageRepository.save(PostImage.builder().post(post).build());

        // when
        List<PostImage> postImages = postImageRepository.findByPostId(1L);

        // then
        assertThat(postImages.size()).isEqualTo(3);
    }

}