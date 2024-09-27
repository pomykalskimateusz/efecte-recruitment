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
    public UUID insert(Long accountId, String content) {
        var postId = UUID.randomUUID();
        dslContext.insertInto(POST)
                .set(POST.ID, UUID.randomUUID())
                .set(POST.ACCOUNT_ID, accountId)
                .set(POST.CONTENT, content)
                .set(POST.VERSION, 1)
                .execute();
        return postId;
    }

    @Transactional
    public void update(UUID postId, String content, int version) {
        var conflict = dslContext.update(POST)
                .set(POST.CONTENT, content)
                .set(POST.VERSION, POST.VERSION.plus(1))
                .where(POST.ID.eq(postId))
                .and(POST.VERSION.eq(version))
                .execute() == 0;
        if(conflict) {
            throw new ConflictException();
        }
    }

    @Transactional
    public void delete(UUID postId, int version) {
        dslContext.deleteFrom(POST)
                .where(POST.ID.eq(postId))
                .returningResult(POST.VERSION)
                .fetchOptional(POST.VERSION)
                .ifPresent(newVersion -> {
                    if (!Objects.equals(newVersion, version)) {
                        throw new ConflictException();
                    }
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
