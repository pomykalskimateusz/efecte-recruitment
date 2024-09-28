package com.efecte.efecterecruitment.post;

import com.efecte.efecterecruitment.DatabaseContainer;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
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
        // GIVEN mocked session account id and post content
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
        // GIVEN mocked session account id and post content with length 250
        var sessionAccountId = 1L;
        var content = generatePostContent(contentLength);

        // WHEN saving post with invalid content length THEN exception should be thrown
        assertThrows(IllegalArgumentException.class, () -> postService.savePost(sessionAccountId, content));
    }

    private String generatePostContent(int length) {
        return RandomStringUtils.random(length, true, true);
    }
}
