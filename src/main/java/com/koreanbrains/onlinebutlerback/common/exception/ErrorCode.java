package com.koreanbrains.onlinebutlerback.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    FILE_NOT_UPLOADED(500, "S3_001", "파일을 업로드하지 못했습니다."),
    
    
    ;
    
    private final int status;
    private final String code;
    private final String message;
    
    
}
