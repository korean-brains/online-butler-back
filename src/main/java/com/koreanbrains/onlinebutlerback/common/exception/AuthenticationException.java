package com.koreanbrains.onlinebutlerback.common.exception;

public class AuthenticationException extends BaseException {

    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
