package com.koreanbrains.onlinebutlerback.common.exception;

public record ErrorResponse (
  String code,
  String message
){
    public ErrorResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getMessage());
    }
}
