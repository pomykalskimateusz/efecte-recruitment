package com.efecte.efecterecruitment.post;

import com.efecte.efecterecruitment.api.PostsApi;
import com.efecte.efecterecruitment.model.CreatePostBody;
import com.efecte.efecterecruitment.security.UserContextMock;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PostController implements PostsApi {
    PostService postService;

    @Override
    public ResponseEntity<Void> createPost(CreatePostBody body) {
        postService.savePost(UserContextMock.getAccountId(), body.getContent());
        return ResponseEntity.ok().build();
    }
}
