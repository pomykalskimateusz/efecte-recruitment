package com.efecte.efecterecruitment.post;

import com.efecte.efecterecruitment.exception.ResourceNotFoundException;
import com.efecte.efecterecruitment.model.Post;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class PostService {
    PostRepository postRepository;

    @Transactional
    public Post savePost(Long accountId, String content) {
        ensureContentIsValid(content);

        return postRepository
                .insert(accountId, content)
                .orElseThrow(RuntimeException::new);
    }

    @Transactional
    public Post updatePost(Long accountId, UUID postId, String content, int version) {
        ensureContentIsValid(content);

        return postRepository
                .update(accountId, postId, content, version)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional
    public UUID deletePost(Long accountId, UUID postId, int version) {
        return postRepository
                .delete(accountId, postId, version)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional
    public Post fetchPost(Long accountId, UUID postId) {
        return postRepository
                .findByPostId(accountId, postId)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional
    public List<Post> fetchPostList(Long accountId) {
        return postRepository.findByAccountId(accountId);
    }

    private void ensureContentIsValid(String content) {
        if (content == null || content.isEmpty() || content.length() > 200) {
            throw new IllegalArgumentException("Incorrect post content value. Should be not empty string value with max length 200");
        }
    }
}
