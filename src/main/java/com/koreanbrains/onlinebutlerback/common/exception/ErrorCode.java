package com.koreanbrains.onlinebutlerback.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //401
    UN_AUTHORIZE(401, "AUTH_001", "인증되지 않음"),
    NO_AUTHORITIES_KEY(401, "JWT_001", "JWT에 권한 정보가 없습니다."),
    INVALID_SIGNATURE(401, "JWT_002", "잘못된 JWT 서명입니다."),
    EXPIRED_TOKEN(401, "JWT_003", "만료된 JWT 입니다."),
    UNSUPPORTED_TOKEN(401, "JWT_004", "지원되지 않는 JWT 입니다."),
    INVALID_TOKEN(401, "JWT_005", "JWT가 잘못되었습니다."),
    NO_AUTHENTICATION_INFO(401, "JWT_006", "인증정보가 없습니다."),
    INVALID_REFRESH_TOKEN(401, "JWT_007", "유효하지 않은 리프레시 토큰입니다."),

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
    DONATION_BOOTPAY_ACCESSTOKEN(500, "DN_001", "엑세스 토큰을 가져오지 못했습니다."),
    DONATION_BOOTPAY_ERORR(500, "DN_002", "부트페이 관련 에러가 발생했습니다."),
    DONATION_BOOTPAY_CONFIRM_FAIL(400, "DN_003", "결제 검증에 실패했습니다."),
    DONATION_HISTORY_INVALID_PAGE_NUMBER(400, "DN_004", "유효하지 않은 페이지 번호입니다."),
    DONATION_HISTORY_INVALID_GIVER_ID(400, "DN_005", "유효하지 않은 기부자 ID입니다."),
    DONATION_HISTORY_INVALID_RECEIVER_ID(400, "DN_005", "유효하지 않은 수령자 ID입니다."),
      
    FILE_NOT_UPLOADED(500, "F_001", "파일을 업로드하지 못했습니다."),
    FILE_NOT_DELETED(500, "F_002", "파일을 삭제하지 못했습니다."),
    FILE_INVALID(400, "F_003", "유효하지 않은 파일입니다."),
    FILE_NOT_LOADED(500, "F_004", "파일을 로드할 수 없습니다."),

    ALREADY_FOLLOWED(400, "FW_001", "이미 팔로우한 사용자입니다."),
    ALREADY_UNFOLLOWED(400, "FW_002", "이미 언팔로우한 사용자입니다."),

    ;

    private final int status;
    private final String code;
    private final String message;
    
    
}
