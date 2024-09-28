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

import static com.efecte.efecterecruitment.jooq.entity.Tables.POST;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class PostRepository {
    DSLContext dslContext;

    @Transactional
    public Optional<Post> insert(Long accountId, String content) {
        return dslContext.insertInto(POST)
                .set(POST.ID, UUID.randomUUID())
                .set(POST.ACCOUNT_ID, accountId)
                .set(POST.CONTENT, content)
                .set(POST.VERSION, 1)
                .returningResult(POST.asterisk())
                .fetchOptional()
                .map(dbRecord -> dbRecord.into(Post.class));
    }

    @Transactional
    public Optional<Post> update(Long accountId, UUID postId, String content, int version) {
        return dslContext.update(POST)
                .set(POST.CONTENT, content)
                .set(POST.VERSION, POST.VERSION.plus(1))
                .where(POST.ID.eq(postId))
                .and(POST.ACCOUNT_ID.eq(accountId))
                .returning(POST.asterisk())
                .fetchOptional()
                .map(dbRecord -> {
                    if ((dbRecord.get(POST.VERSION) - 1) != version) {
                        throw new ConflictException();
                    }
                    return dbRecord.into(Post.class);
                });
    }

    @Transactional
    public Optional<UUID> delete(Long accountId, UUID postId, int version) {
        return dslContext.deleteFrom(POST)
                .where(POST.ID.eq(postId))
                .and(POST.ACCOUNT_ID.eq(accountId))
                .returningResult(POST.ID, POST.VERSION)
                .fetchOptional()
                .map(dbRecord -> {
                    if (!Objects.equals(dbRecord.get(POST.VERSION), version)) {
                        throw new ConflictException();
                    }
                    return dbRecord.get(POST.ID);
                });
    }

    @Transactional
    public Optional<Post> findByPostId(Long accountId, UUID postId) {
        return dslContext.select(POST.asterisk())
                .from(POST)
                .where(POST.ID.eq(postId))
                .and(POST.ACCOUNT_ID.eq(accountId))
                .fetchOptional()
                .map(dbRecord -> dbRecord.into(Post.class));
    }

    @Transactional
    public List<Post> findByAccountId(Long accountId) {
        return dslContext.select(POST.asterisk())
                .from(POST)
                .where(POST.ACCOUNT_ID.eq(accountId))
                .fetch()
                .map(dbRecord -> dbRecord.into(Post.class));
    }
}
