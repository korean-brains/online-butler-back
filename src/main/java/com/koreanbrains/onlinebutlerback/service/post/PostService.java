package com.koreanbrains.onlinebutlerback.service.post;

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

        postRepository.save(post);

        for (MultipartFile image : images) {
            UploadFile uploadFile = s3Client.upload(image);

            PostImage postImage = PostImage.builder()
                    .post(post)
                    .url(uploadFile.url())
                    .originalName(uploadFile.originalFilename())
                    .storedName(uploadFile.storeFilename())
                    .build();

            postImageRepository.save(postImage);
        }

        return post.getId();
    }


}
