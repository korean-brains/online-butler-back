package com.koreanbrains.onlinebutlerback.common.security.util;

import com.koreanbrains.onlinebutlerback.common.exception.ErrorCode;
import com.koreanbrains.onlinebutlerback.common.exception.PermissionDeniedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtil {
  public static Long getAuthenticatedMemberId() {
      final Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

      if (authentication == null || authentication.getName() == null) {
          throw new PermissionDeniedException(ErrorCode.NO_AUTHENTICATION_INFO);
      }

      return Long.parseLong(authentication.getName());
  }
}
