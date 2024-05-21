package com.koreanbrains.onlinebutlerback.service.post;

import com.koreanbrains.onlinebutlerback.common.exception.EntityNotFoundException;
import com.koreanbrains.onlinebutlerback.common.exception.IOException;
import com.koreanbrains.onlinebutlerback.common.exception.PermissionDeniedException;
import com.koreanbrains.onlinebutlerback.common.fixtures.MemberFixture;
import com.koreanbrains.onlinebutlerback.common.fixtures.PostFixture;
import com.koreanbrains.onlinebutlerback.common.fixtures.PostImageFixture;
import com.koreanbrains.onlinebutlerback.common.util.file.FileStore;
import com.koreanbrains.onlinebutlerback.common.util.file.UploadFile;
import com.koreanbrains.onlinebutlerback.entity.post.Post;
import com.koreanbrains.onlinebutlerback.entity.post.PostImage;
import com.koreanbrains.onlinebutlerback.repository.comment.CommentRepository;
import com.koreanbrains.onlinebutlerback.repository.member.MemberRepository;
import com.koreanbrains.onlinebutlerback.repository.post.PostImageRepository;
import com.koreanbrains.onlinebutlerback.repository.post.PostRepository;
import com.koreanbrains.onlinebutlerback.service.tag.TagService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks
    PostService postService;

    @Mock
    FileStore fileStore;
    @Mock
    PostRepository postRepository;
    @Mock
    PostImageRepository postImageRepository;
    @Mock
    TagService tagService;
    @Mock
    MemberRepository memberRepository;


    @Test
    @DisplayName("포스트가 생성되는지 확인한다.")
    void createPost() {
        // given
        given(fileStore.upload(any(), anyString())).willReturn(new UploadFile("cat.jpg",
                "9257a629-f0a3-4fc4-8264-77baf3092644.jpg",
                "https://online-butler-s3.s3.ap-northeast-2.amazonaws.com/9257a629-f0a3-4fc4-8264-77baf3092644.jpg"));

        String caption = "포스트 내용";
        MockMultipartFile[] images = {
                new MockMultipartFile("images",
                        "cat.jpg",
                        MediaType.IMAGE_PNG_VALUE,
                        "<<image>>".getBytes())};
        String[] tags = {"고양이", "뚱냥이"};

        given(postRepository.save(any())).willReturn(Post.builder().id(1L).build());
        given(postImageRepository.save(any())).willReturn(PostImage.builder().build());
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(MemberFixture.member()));
        doNothing().when(tagService).linkTags(any(), any());

        // when
        Long postId = postService.createPost(caption, images, tags, 1L);

        // then
        assertThat(postId).isEqualTo(1L);
    }

    @Test
    @DisplayName("포스트 생성시 작성자가 존재하지 않으면 예외가 발생한다.")
    void createPostFailNotFoundWriter() {
        // given
        String caption = "포스트 내용";
        MockMultipartFile[] images = {
                new MockMultipartFile("images",
                        "cat.jpg",
                        MediaType.IMAGE_PNG_VALUE,
                        "<<image>>".getBytes())};
        String[] tags = {"고양이", "뚱냥이"};

        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> postService.createPost(caption, images, tags, 1L))
                .isInstanceOf(EntityNotFoundException.class);
    }
    
    @Test
    @DisplayName("이미지가 업로드 되지 않으면 예외가 발생한다")
    void failUploadImage() {
        // given
        given(fileStore.upload(any(), anyString())).willThrow(IOException.class);
        given(postRepository.save(any())).willReturn(Post.builder().id(1L).build());
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(MemberFixture.member()));

        String caption = "포스트 내용";
        MockMultipartFile[] images = {
                new MockMultipartFile("images",
                        "cat.jpg",
                        MediaType.IMAGE_PNG_VALUE,
                        "<<image>>".getBytes())};
        String[] tags = {"고양이", "뚱냥이"};
        
        // when
        // then
        assertThatThrownBy(() -> postService.createPost(caption, images, tags, 1L))
                .isInstanceOf(IOException.class);
    }

    @Test
    @DisplayName("포스트 내용을 수정한다")
    void updatePost() {
        // given
        Post post = PostFixture.post(1L, 1L);
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));

        // when
        postService.updatePost(1L, "수정된 포스트 내용", new String[]{"뚱냥이", "고양이"}, 1L);

        // then
        assertThat(post.getCaption()).isEqualTo("수정된 포스트 내용");
    }

    @Test
    @DisplayName("수정하려는 포스트가 존재하지 않으면 예외가 발생한다")
    void failUpdatePost() {
        // given
        given(postRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> postService.updatePost(1L, "수정된 포스트 내용", new String[]{"뚱냥이", "고양이"}, 1L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("수정하려는 사람과 포스트의 작성자가 다르면 예외가 발생한다")
    void failUpdatePostWriter() {
        // given
        Post post = PostFixture.post(1L, 1L);
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));

        // when
        // then
        assertThatThrownBy(() -> postService.updatePost(1L, "수정된 포스트 내용", new String[]{"뚱냥이", "고양이"}, 2L))
                .isInstanceOf(PermissionDeniedException.class);
    }

    @Test
    @DisplayName("포스트를 삭제한다")
    void deletePost() {
        // given
        Post post = PostFixture.post(1L, 1L);
        List<PostImage> postImages = List.of(
                PostImageFixture.postImage(1L, post),
                PostImageFixture.postImage(2L, post),
                PostImageFixture.postImage(3L, post)
                );
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(postImageRepository.findByPostId(anyLong())).willReturn(postImages);
        given(fileStore.delete(anyString())).willReturn(true);
        doNothing().when(postRepository).delete(any());

        // when
        postService.deletePost(1L, 1L);

        // then
        then(postRepository).should().delete(post);
        then(fileStore).should(times(3)).delete(anyString());
    }

    @Test
    @DisplayName("포스트를 삭제하려는 사람과 작성자가 다르면 예외가 발생한다")
    void failDeletePostWriter() {
        // given
        Post post = PostFixture.post(1L, 1L);
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));

        // when
        // then
        assertThatThrownBy(() -> postService.deletePost(1L, 2L))
                .isInstanceOf(PermissionDeniedException.class);
    }
}