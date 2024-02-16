package com.koreanbrains.onlinebutlerback.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //400
    ALREADY_MEMBER_IN(400, "M_002", "이미 가입된 멤버입니다."),

    //401
    NO_AUTHORITIES_KEY(401, "JWT_001", "JWT에 권한 정보가 없습니다."),
    INVALID_SIGNATURE(401, "JWT_002", "잘못된 JWT 서명입니다."),
    EXPIRED_TOKEN(401, "JWT_003", "만료된 JWT 입니다."),
    UNSUPPORTED_TOKEN(401, "JWT_004", "지원되지 않는 JWT 입니다."),
    INVALID_TOKEN(401, "JWT_005", "JWT가 잘못되었습니다."),
    NO_AUTHENTICATION_INFO(401, "JWT_006", "인증정보가 없습니다."),

    //403
    PERMISSION_DENIED(403, "PM_001", "권한이 없습니다."),

    //404
    POST_NOT_FOUND(404, "P_001", "포스트를 찾을 수 없습니다."),
    MEMBER_NOT_FOUND(404, "M_001", "멤버를 찾을 수 없습니다."),

    //500
    FILE_NOT_UPLOADED(500, "S3_001", "파일을 업로드하지 못했습니다."),

    ;
    
    private final int status;
    private final String code;
    private final String message;
    
    
}
