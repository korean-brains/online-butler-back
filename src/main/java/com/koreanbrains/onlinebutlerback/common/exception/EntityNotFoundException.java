package com.koreanbrains.onlinebutlerback.common.exception;

public class EntityNotFoundException extends BaseException {

    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public EntityNotFoundException(Throwable cause, ErrorCode errorCode) {
        super(cause, errorCode);
    }
}
