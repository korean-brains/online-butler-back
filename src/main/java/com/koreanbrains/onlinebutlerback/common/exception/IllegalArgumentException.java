package com.koreanbrains.onlinebutlerback.common.exception;

public class IllegalArgumentException extends BaseException{

    public IllegalArgumentException(ErrorCode errorCode) {
        super(errorCode);
    }

    public IllegalArgumentException(Throwable cause, ErrorCode errorCode) {
        super(cause, errorCode);
    }
}
