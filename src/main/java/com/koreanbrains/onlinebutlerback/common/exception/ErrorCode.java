package com.koreanbrains.onlinebutlerback.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //400

    //403
    PERMISSION_DENIED(403, "PM_001", "권한이 없습니다."),

    //404
    POST_NOT_FOUND(404, "P_001", "포스트를 찾을 수 없습니다."),
    MEMBER_NOT_FOUND(404, "M_001", "멤버를 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(404, "C_001", "댓글을 찾을 수 없습니다."),
    REPLY_NOT_FOUND(404, "RC_001", "답글을 찾을 수없습니다."),
    LIKE_NOT_FOUND(404, "L_001", "좋아요를 찾을 수 없습니다."),
    ALREADY_LIKE_POST(400, "L_002", "이미 좋아요를 누른 게시글입니다."),

    //500
    FILE_NOT_UPLOADED(500, "F_001", "파일을 업로드하지 못했습니다."),
    FILE_NOT_DELETED(500, "F_002", "파일을 삭제하지 못했습니다."),
    FILE_INVALID(400, "F_003", "유효하지 않은 파일입니다."),
    FILE_NOT_LOADED(500, "F_004", "파일을 로드할 수 없습니다."),

    ;

    private final int status;
    private final String code;
    private final String message;
    
    
}
