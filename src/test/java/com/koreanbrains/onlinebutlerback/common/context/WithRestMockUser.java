package com.koreanbrains.onlinebutlerback.common.context;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithRestMockUserSecurityContextFactory.class)
public @interface WithRestMockUser {

    long id() default 1L;
    String email() default "test@t.t";
    String name() default "testUser";
}
