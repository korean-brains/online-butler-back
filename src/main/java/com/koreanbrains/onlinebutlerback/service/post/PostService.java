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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final S3Client s3Client;

    @Transactional
    public Long createPost(String caption, MultipartFile[] images) {
        Post post = Post.builder()
                .caption(caption)
                .build();

        Long postId = postRepository.save(post).getId();

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
    public void updatePost(Long postId, String caption, Long memberId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.POST_NOT_FOUND));

        if (!Objects.equals(post.getMemberId(), memberId)) {
            throw new PermissionDeniedException(ErrorCode.PERMISSION_DENIED);
        }

        post.changeCaption(caption);
    }


}
