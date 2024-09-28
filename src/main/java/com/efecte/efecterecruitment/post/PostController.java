package com.efecte.efecterecruitment.post;

import com.efecte.efecterecruitment.api.PostsApi;
import com.efecte.efecterecruitment.model.CreatePostBody;
import com.efecte.efecterecruitment.model.DeletePostResponse;
import com.efecte.efecterecruitment.model.Post;
import com.efecte.efecterecruitment.model.UpdatePostBody;
import com.efecte.efecterecruitment.security.UserContextMock;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PostController implements PostsApi {
    static String INDEX_HTML_PATH = "/web/browser/index.html";
    static String CSS_HTML_PATH = "/web/browser/styles-5INURTSO.css";
    static String JS_1_HTML_PATH = "/web/browser/polyfills-FFHMD2TL.js";
    static String JS_2_HTML_PATH = "/web/browser/main-A5V3K3FC.js";

    PostService postService;

    @GetMapping("/styles-5INURTSO.css")
    public String css() {
        return CSS_HTML_PATH;
    }

    @GetMapping("/polyfills-FFHMD2TL.js")
    public String js_1() {
        return JS_1_HTML_PATH;
    }

    @GetMapping("/main-A5V3K3FC.js")
    public String js_2() {
        return JS_2_HTML_PATH;
    }

    @GetMapping("/application")
    public String index() {
        return INDEX_HTML_PATH;
    }

    @Override
    public ResponseEntity<Post> createPost(CreatePostBody body) {
        var post = postService.savePost(UserContextMock.getAccountId(), body.getContent());
        return ResponseEntity.ok(post);
    }

    @Override
    public ResponseEntity<List<Post>> readPostList() {
        var postList = postService.fetchPostList(UserContextMock.getAccountId());
        return ResponseEntity.ok(postList);
    }

    @Override
    public ResponseEntity<Post> readPost(UUID postId) {
        var post = postService.fetchPost(UserContextMock.getAccountId(), postId);
        return ResponseEntity.ok(post);
    }

    @Override
    public ResponseEntity<Post> updatePost(UUID postId, UpdatePostBody body) {
        var post = postService.updatePost(UserContextMock.getAccountId(), postId, body.getContent(), body.getVersion());
        return ResponseEntity.ok(post);
    }

    @Override
    public ResponseEntity<DeletePostResponse> deletePost(UUID postId, Integer version) {
        var deletedPostId = postService.deletePost(UserContextMock.getAccountId(), postId, version);
        return ResponseEntity.ok(new DeletePostResponse().postId(deletedPostId));
    }
}
