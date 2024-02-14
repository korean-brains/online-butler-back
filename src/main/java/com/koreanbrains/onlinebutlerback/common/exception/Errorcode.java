package com.koreanbrains.onlinebutlerback.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    FILE_NOT_UPLOADED(500, "S3_001", "파일을 업로드하지 못했습니다."),

    POST_NOT_FOUND(404, "P_001", "포스트를 찾을 수 없습니다."),
    MEMBER_NOT_FOUND(404, "M_001", "멤버를 찾을 수 없습니다."),

    PERMISSION_DENIED(403, "PM_001", "권한이 없습니다."),

    ;
    
    private final int status;
    private final String code;
    private final String message;
    
    
}
