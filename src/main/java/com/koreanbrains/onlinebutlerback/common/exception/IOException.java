package com.koreanbrains.onlinebutlerback.common.exception;

public class IOException extends BaseException {

    public IOException(ErrorCode errorCode) {
        super(errorCode);
    }

    public IOException(Throwable cause, ErrorCode errorCode) {
        super(cause, errorCode);
    }
}
