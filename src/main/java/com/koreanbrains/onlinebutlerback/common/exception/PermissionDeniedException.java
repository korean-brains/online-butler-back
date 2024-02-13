package com.koreanbrains.onlinebutlerback.common.exception;

public class PermissionDeniedException extends BaseException{

    public PermissionDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }

    public PermissionDeniedException(Throwable cause, ErrorCode errorCode) {
        super(cause, errorCode);
    }
}
