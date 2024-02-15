package com.koreanbrains.onlinebutlerback.service.post;

import com.koreanbrains.onlinebutlerback.common.exception.EntityNotFoundException;
import com.koreanbrains.onlinebutlerback.common.exception.ErrorCode;
import com.koreanbrains.onlinebutlerback.common.exception.PermissionDeniedException;
import com.koreanbrains.onlinebutlerback.common.util.s3.S3Client;
import com.koreanbrains.onlinebutlerback.common.util.s3.UploadFile;
import com.koreanbrains.onlinebutlerback.entity.post.Post;
import com.koreanbrains.onlinebutlerback.entity.post.PostImage;
import com.koreanbrains.onlinebutlerback.repository.post.PostImageRepository;
import com.koreanbrains.onlinebutlerback.repository.post.PostRepository;
import com.koreanbrains.onlinebutlerback.service.tag.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final TagService tagService;
    private final S3Client s3Client;

    @Transactional
    public Long createPost(String caption, MultipartFile[] images, String[] tags) {
        Post post = Post.builder()
                .caption(caption)
                .build();
        Long postId = postRepository.save(post).getId();

        tagService.linkTags(post, tags);

        for (MultipartFile image : images) {
            UploadFile uploadFile = s3Client.upload(image, UUID.randomUUID().toString());

            PostImage postImage = PostImage.builder()
                    .post(post)
                    .url(uploadFile.url())
                    .originalName(uploadFile.originalFilename())
                    .storedName(uploadFile.storeFilename())
                    .build();

            postImageRepository.save(postImage);
        }

        return postId;
    }

    @Transactional
    public void updatePost(Long postId, String caption, String[] tags, Long memberId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.POST_NOT_FOUND));

        if (!Objects.equals(post.getMemberId(), memberId)) {
            throw new PermissionDeniedException(ErrorCode.PERMISSION_DENIED);
        }

        post.changeCaption(caption);
        tagService.resetTags(post.getId());
        tagService.linkTags(post, tags);
    }

    @Transactional
    public void deletePost(Long postId, Long memberId) {
        postRepository.findById(postId).ifPresent(post -> {
            if(!Objects.equals(post.getMemberId(), memberId))
                throw new PermissionDeniedException(ErrorCode.PERMISSION_DENIED);

            List<PostImage> postImages = postImageRepository.findByPostId(postId);
            for (PostImage postImage : postImages) {
                s3Client.delete(postImage.getStoredName());
            }
            postImageRepository.deleteAll(postImages);
            postRepository.delete(post);
        });
    }

}
