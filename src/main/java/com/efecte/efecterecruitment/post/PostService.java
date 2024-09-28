package com.efecte.efecterecruitment.post;

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
    public UUID savePost(Long accountId, String content) {
        ensureContentIsValid(content);

        return postRepository.insert(accountId, content);
    }

    @Transactional
    public void updatePost(Long accountId, UUID postId, String content, int version) {
        ensureContentIsValid(content);
        ensurePostCanBeAccessed(accountId, postId);

        postRepository.update(postId, content, version);
    }

    @Transactional
    public void deletePost(Long accountId, UUID postId, int version) {
        ensurePostCanBeAccessed(accountId, postId);

        postRepository.delete(postId, version);
    }

    @Transactional
    public Post fetchPost(Long accountId, UUID postId) {
        return postRepository
                .findByPostId(accountId, postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found: " + postId));
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

    private void ensurePostCanBeAccessed(Long accountId, UUID postId) {
        postRepository
                .findByPostId(accountId, postId)
                .orElseThrow(() -> new IllegalArgumentException("Session account does not have access to post: " + postId));
    }
}
