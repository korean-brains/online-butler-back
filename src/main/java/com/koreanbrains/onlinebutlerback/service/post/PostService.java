package com.koreanbrains.onlinebutlerback.service.post;

import com.koreanbrains.onlinebutlerback.common.exception.EntityNotFoundException;
import com.koreanbrains.onlinebutlerback.common.exception.ErrorCode;
import com.koreanbrains.onlinebutlerback.common.exception.PermissionDeniedException;
import com.koreanbrains.onlinebutlerback.common.util.file.FileStore;
import com.koreanbrains.onlinebutlerback.common.util.file.UploadFile;
import com.koreanbrains.onlinebutlerback.entity.member.Member;
import com.koreanbrains.onlinebutlerback.entity.post.Post;
import com.koreanbrains.onlinebutlerback.entity.post.PostImage;
import com.koreanbrains.onlinebutlerback.repository.member.MemberRepository;
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
    private final FileStore fileStore;
    private final MemberRepository memberRepository;

    @Transactional
    public Long createPost(String caption, MultipartFile[] images, String[] tags, Long writerId) {
        Member member = memberRepository.findById(writerId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        Post post = Post.builder()
                .caption(caption)
                .writer(member)
                .build();
        Long postId = postRepository.save(post).getId();

        tagService.linkTags(post, tags);

        for (MultipartFile image : images) {
            UploadFile uploadFile = fileStore.upload(image, UUID.randomUUID().toString());

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

        if (!Objects.equals(post.getWriter().getId(), memberId)) {
            throw new PermissionDeniedException(ErrorCode.PERMISSION_DENIED);
        }

        post.changeCaption(caption);
        tagService.resetTags(post.getId());
        tagService.linkTags(post, tags);
    }

    @Transactional
    public void deletePost(Long postId, Long memberId) {
        postRepository.findById(postId).ifPresent(post -> {
            if(!Objects.equals(post.getWriter().getId(), memberId))
                throw new PermissionDeniedException(ErrorCode.PERMISSION_DENIED);

            List<PostImage> postImages = postImageRepository.findByPostId(postId);
            for (PostImage postImage : postImages) {
                    fileStore.delete(postImage.getStoredName());
            }

            postRepository.delete(post);
        });
    }

}
