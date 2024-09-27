package com.efecte.efecterecruitment.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserContextMock {
    private static final Long SESSION_CONTEXT_ACCOUNT_ID = 1L;

    public static Long getAccountId() {
        return SESSION_CONTEXT_ACCOUNT_ID;
    }
}
