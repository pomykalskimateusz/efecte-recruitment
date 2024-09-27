package com.efecte.efecterecruitment.post;

import com.efecte.efecterecruitment.DatabaseContainer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostServiceTest extends DatabaseContainer {
    @Autowired
    PostService postService;

    @BeforeEach
    void afterEach() {
        super.cleanDatabase("posts", false);
    }

    @Test
    void shouldCreatePost() {
        // GIVEN mocked session account id and post content
        var sessionAccountId = 1L;
        var content = "import static org.junit.jupiter.api.Assertions.assertTrue;";

        // WHEN saving post
        var postId = postService.savePost(sessionAccountId, content);

        // THEN post should be saved and available to read by session account
        var post = postService.fetchPost(sessionAccountId, postId);
        assertEquals(postId, post.getId());
        assertEquals(content, post.getContent());
        assertEquals(1, post.getVersion());
    }
}
