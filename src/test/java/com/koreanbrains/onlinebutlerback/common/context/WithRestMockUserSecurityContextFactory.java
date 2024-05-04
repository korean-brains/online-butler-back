package com.koreanbrains.onlinebutlerback.common.context;

import com.koreanbrains.onlinebutlerback.common.security.dto.AccountContext;
import com.koreanbrains.onlinebutlerback.common.security.dto.AccountDto;
import com.koreanbrains.onlinebutlerback.common.security.token.RestAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithRestMockUserSecurityContextFactory implements WithSecurityContextFactory<WithRestMockUser> {

    @Override
    public SecurityContext createSecurityContext(WithRestMockUser annotation) {
        AccountContext accountContext = new AccountContext(AccountDto.builder()
                .id(annotation.id())
                .name(annotation.name())
                .email(annotation.email())
                .password(null)
                .role("ROLE_USER")
                .isActive(true)
                .build());

        Authentication authentication = new RestAuthenticationToken(accountContext.getAccountDto(), null, accountContext.getAuthorities());
        SecurityContext context = SecurityContextHolder.getContextHolderStrategy().createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }
}
