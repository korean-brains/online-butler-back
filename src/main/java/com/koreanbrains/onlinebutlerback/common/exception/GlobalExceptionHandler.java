package com.koreanbrains.onlinebutlerback.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> baseExHandle(BaseException e) {
        ErrorCode errorCode = e.getErrorCode();
        return new ResponseEntity<>(new ErrorResponse(errorCode),
                HttpStatus.valueOf(errorCode.getStatus()));
    }
}
