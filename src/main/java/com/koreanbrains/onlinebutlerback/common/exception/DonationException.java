package com.koreanbrains.onlinebutlerback.common.exception;

public class DonationException extends BaseException {

    public DonationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public DonationException(Throwable cause, ErrorCode errorCode) {
        super(cause, errorCode);
    }
}
