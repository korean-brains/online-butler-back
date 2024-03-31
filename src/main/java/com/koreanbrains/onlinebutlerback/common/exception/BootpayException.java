package com.koreanbrains.onlinebutlerback.common.exception;

public class BootpayException extends BaseException {

    public BootpayException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BootpayException(Throwable cause, ErrorCode errorCode) {
        super(cause, errorCode);
    }
}
