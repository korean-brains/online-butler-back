package com.koreanbrains.onlinebutlerback.common.exception;

public class EntityExistsException extends BaseException {

    public EntityExistsException(ErrorCode errorCode) {
        super(errorCode);
    }

    public EntityExistsException(Throwable cause, ErrorCode errorCode) {
        super(cause, errorCode);
    }
}
