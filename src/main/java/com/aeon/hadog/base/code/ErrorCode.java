package com.aeon.hadog.base.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    /*
     * 400 BAD_REQUEST: 잘못된 요청
     */
    DUPLICATE_ID_REQUEST(HttpStatus.BAD_REQUEST, "중복된 아이디가 있습니다."),
    DUPLICATE_NICKNAME_REQUEST(HttpStatus.BAD_REQUEST, "중복된 닉네임이 있습니다."),
    DUPLICATE_EMAIL_REQUEST(HttpStatus.BAD_REQUEST, "중복된 이메일이 있습니다."),

    /*
     * 403 FORBIDDEN : 서버가 해당 요청 거부
     */
    INVALID_LOGIN(HttpStatus.FORBIDDEN, "아이디 또는 비밀번호가 잘못 입력되었습니다."),
    PASSWORD_MISMATCH(HttpStatus.FORBIDDEN, "비밀번호가 잘못 입력되었습니다."),
    PASSWORD_FORMAT_INVALID(HttpStatus.FORBIDDEN, "새 비밀번호는 영문, 숫자, 특수문자를 포함하고 8~16자여야 합니다."),
    NEW_PASSWORD_SAME_AS_OLD(HttpStatus.FORBIDDEN, "새 비밀번호는 이전 비밀번호와 다르게 설정해야 합니다."),
    BLANK_CONTENT_ERROR(HttpStatus.FORBIDDEN, "일기 내용은 필수로 작성해야 합니다."),
    EMOTION_TRACK_NOT_BELONG_TO_USER_ERROR(HttpStatus.FORBIDDEN, "사용자의 펫 ID와 감정 트랙의 펫 ID가 일치하지 않습니다."),
    ADOPT_POST_NOT_BELONG_TO_USER_ERROR(HttpStatus.FORBIDDEN, "사용자가 작성하지 않은 임시보호 공고글 입니다"),


    /*
     * 404 NOT_FOUND : 잘못된 리소스 접근
     */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 유저가 존재하지 않습니다"),
    EMOTIONTRACK_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 감정 분석 결과가 존재하지 않습니다"),
    DIARY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 일기가 존재하지 않습니다"),
    PET_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 반려견이 존재하지 않습니다"),

    SHELTER_POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 보호소 공고가 존재하지 않습니다"),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 댓글이 존재하지 않습니다."),
    ADOPT_POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 임시보호 공고가 존재하지 않습니다."),
    ADOPT_REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 리뷰가 존재하지 않습니다.");

    /*
     * 500 INTERNAL SERVER ERROR : 서버 에러
     */

    private final HttpStatus status;
    private final String message;
}
