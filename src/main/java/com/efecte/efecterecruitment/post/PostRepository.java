package com.efecte.efecterecruitment.post;

import com.efecte.efecterecruitment.exception.ConflictException;
import com.efecte.efecterecruitment.model.Post;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.efecte.efecterecruitment.jooq.entity.Tables.POSTS_;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class PostRepository {
    DSLContext dslContext;

    @Transactional
    public void insert(Long accountId, String content) {
        dslContext.insertInto(POSTS_)
                .set(POSTS_.ID, UUID.randomUUID())
                .set(POSTS_.ACCOUNT_ID, accountId)
                .set(POSTS_.CONTENT, content)
                .set(POSTS_.VERSION, 1)
                .execute();
    }

    @Transactional
    public void update(UUID postId, String content, int version) {
        var conflict = dslContext.update(POSTS_)
                .set(POSTS_.CONTENT, content)
                .set(POSTS_.VERSION, POSTS_.VERSION.plus(1))
                .where(POSTS_.ID.eq(postId))
                .and(POSTS_.VERSION.eq(version))
                .execute() == 0;
        if(conflict) {
            throw new ConflictException();
        }
    }

    @Transactional
    public void delete(UUID postId, int version) {
        dslContext.deleteFrom(POSTS_)
                .where(POSTS_.ID.eq(postId))
                .returningResult(POSTS_.VERSION)
                .fetchOptional(POSTS_.VERSION)
                .ifPresent(newVersion -> {
                    if (!Objects.equals(newVersion, version)) {
                        throw new ConflictException();
                    }
                });
    }

    @Transactional
    public Optional<Post> findByPostId(Long accountId, UUID postId) {
        return dslContext.select(POSTS_.asterisk())
                .from(POSTS_)
                .where(POSTS_.ID.eq(postId))
                .and(POSTS_.ACCOUNT_ID.eq(accountId))
                .fetchOptional()
                .map(dbRecord -> dbRecord.into(Post.class));
    }

    @Transactional
    public List<Post> findByAccountId(Long accountId) {
        return dslContext.select(POSTS_.asterisk())
                .from(POSTS_)
                .where(POSTS_.ACCOUNT_ID.eq(accountId))
                .fetch()
                .map(dbRecord -> dbRecord.into(Post.class));
    }
}
