package com.efecte.efecterecruitment.post;

import com.efecte.efecterecruitment.DatabaseContainer;
import com.efecte.efecterecruitment.exception.ConflictException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PostServiceTest extends DatabaseContainer {
    @Autowired
    PostService postService;

    @AfterEach
    void afterEach() {
        super.cleanDatabase("posts", false);
    }

    @ParameterizedTest()
    @ValueSource(ints = {100, 150, 200})
    void shouldCreatePostWithValidContentLength(int contentLength) {
        // GIVEN mocked session account id and post content with valid length
        var sessionAccountId = 1L;
        var content = generatePostContent(contentLength);

        // WHEN saving post
        var postId = postService.savePost(sessionAccountId, content);

        // THEN post should be saved and available to read by session account
        var post = postService.fetchPost(sessionAccountId, postId);

        assertEquals(postId, post.getId());
        assertEquals(content, post.getContent());
        assertEquals(1, post.getVersion());
    }

    @ParameterizedTest()
    @ValueSource(ints = {201, 250})
    void shouldThrowExceptionForInvalidContentLengthDuringSavingPost(int contentLength) {
        // GIVEN mocked session account id and post content with invalid length
        var sessionAccountId = 1L;
        var content = generatePostContent(contentLength);

        // WHEN saving post with invalid content length THEN exception should be thrown
        assertThrows(IllegalArgumentException.class, () -> postService.savePost(sessionAccountId, content));
    }

    @ParameterizedTest()
    @ValueSource(ints = {100, 150, 200})
    void shouldUpdatePostWithValidContentLengthAndVersion(int contentLength) {
        // GIVEN mocked session account id and created post
        var sessionAccountId = 1L;
        var postId = createPost(sessionAccountId);

        // WHEN updating post with valid content and correct version (equals 1)
        var content = generatePostContent(contentLength);
        postService.updatePost(sessionAccountId, postId, content, 1);

        // THEN post content should be updated along with version
        var updatedPost = postService.fetchPost(sessionAccountId, postId);
        assertEquals(postId, updatedPost.getId());
        assertEquals(content, updatedPost.getContent());
        assertEquals(2, updatedPost.getVersion());
    }

    @ParameterizedTest()
    @ValueSource(ints = {201, 250})
    void shouldThrowExceptionForInvalidContentLengthDuringUpdatingPost(int contentLength) {
        // GIVEN mocked session account id and created post
        var sessionAccountId = 1L;
        var postId = createPost(sessionAccountId);

        // WHEN updating post with invalid content THEN exception should be thrown
        var content = generatePostContent(contentLength);
        assertThrows(IllegalArgumentException.class, () -> postService.updatePost(sessionAccountId, postId, content, 1));
    }

    @Test
    void shouldThrowExceptionForOutdatedVersionDuringUpdatingPost() {
        // GIVEN mocked session account id and updated post (after update version equals 2)
        var sessionAccountId = 1L;
        var postId = createPost(sessionAccountId);
        postService.updatePost(sessionAccountId, postId, generatePostContent(10), 1);

        // WHEN updating post with outdated version THEN conflict exception should be thrown
        assertThrows(ConflictException.class, () -> postService.updatePost(sessionAccountId, postId, generatePostContent(20), 1));
    }

    @Test
    void shouldThrowExceptionForUpdatingPostByNotOwner() {
        // GIVEN mocked session account id and created post
        var sessionAccountId = 1L;
        var postId = createPost(sessionAccountId);

        // WHEN updating post with differ account id than mocked session account id THEN exception should be thrown
        assertThrows(IllegalArgumentException.class, () -> postService.updatePost(2L, postId, generatePostContent(10), 1));
    }

    @Test
    void shouldThrowExceptionForUpdatingNotExistingPost() {
        assertThrows(IllegalArgumentException.class, () -> postService.updatePost(1L, UUID.randomUUID(), generatePostContent(10), 1));
    }

    @Test
    void shouldDeletePostById() {
        // GIVEN mocked session account id and created two posts
        var sessionAccountId = 1L;
        var postId1 = createPost(sessionAccountId);
        var postId2 = createPost(sessionAccountId);

        var postsBeforeDeleting = postService.fetchPostList(sessionAccountId);
        assertEquals(2, postsBeforeDeleting.size());

        // WHEN deleting post by id
        postService.deletePost(sessionAccountId, postId1, 1);

        // THEN post should not be available and queried. Only post1 should be returned
        var postsAfterDeleting = postService.fetchPostList(sessionAccountId);
        assertEquals(1, postsAfterDeleting.size());
        assertEquals(postId2, postsAfterDeleting.get(0).getId());
    }

    @Test
    void shouldThrowExceptionForDeletingPostByNotOwner() {
        // GIVEN mocked session account id and created two posts
        var sessionAccountId = 1L;
        var postId1 = createPost(sessionAccountId);

        var postsBeforeDeleting = postService.fetchPostList(sessionAccountId);
        assertEquals(1, postsBeforeDeleting.size());

        // WHEN deleting post by not post owner THEN exception should be thrown
        assertThrows(IllegalArgumentException.class, () -> postService.deletePost(2L, postId1, 1));
    }

    @Test
    void shouldThrowExceptionForDeletingPostWithOutdatedVersion() {
        // GIVEN mocked session account id and updated post (version equals 2)
        var sessionAccountId = 1L;
        var postId = createPost(sessionAccountId);
        postService.updatePost(sessionAccountId, postId, generatePostContent(10), 1);

        // WHEN deleting post with outdated version THEN conflict exception should be thrown
        assertThrows(ConflictException.class, () -> postService.deletePost(sessionAccountId, postId, 1));
    }

    @Test
    void shouldThrowExceptionForDeletingNotExistingPost() {
        assertThrows(IllegalArgumentException.class, () -> postService.deletePost(1L, UUID.randomUUID(), 1));
    }

    private UUID createPost(Long accountId) {
        return postService.savePost(accountId, generatePostContent(100));
    }

    private String generatePostContent(int length) {
        return RandomStringUtils.random(length, true, true);
    }
}
