package com.prgrms.offer.common.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResponseMessage {
    // common
    SUCCESS(HttpStatus.OK, "Success"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에서 내부적으로 에러가 발생했습니다."),
    PERMISSION_DENIED(HttpStatus.BAD_REQUEST, "접근 권한이 없습니다."),
    HTTP_REQUEST_METHOD_NOT_SUPPORTED(HttpStatus.BAD_REQUEST, "지원하지 않는 HTTP method 입니다."),
    INVALID_REQUEST_ARGUMENT_TYPE(HttpStatus.BAD_REQUEST, "Request 인자 값의 타입이 올바르지 않습니다."),
    MISSING_PARAMETER(HttpStatus.BAD_REQUEST, "Request Parameter가 비어있습니다."),

    // authentication
    LOGIN_FAIL(HttpStatus.BAD_REQUEST, "email 또는 비밀번호가 일치하지 않습니다."),
    DUPLICATE_EMAIL(HttpStatus.OK, "이미 존재하는 이메일입니다."),
    VALID_EMAIL(HttpStatus.OK, "사용 가능한 이메일입니다."),

    // article
    ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 게시글을 찾을 수 없습니다."),
    PRODUCT_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 상품 상태(중고상품, 새상품)가 올바르지 않습니다."),
    TRADE_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 거래 상태가 존재하지 않습니다."),
    TRADE_METHOD_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 거래방식이 존재하지 않습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리가 존재하지 않습니다."),
    NOT_SUPPORTING_PARAM_COMBINATION(HttpStatus.BAD_REQUEST, "지원하지 않는 parameter 조합입니다."),
    NOT_COMPLETED_TRADE(HttpStatus.BAD_REQUEST, "거래완료상태가 아닙니다."),
    ALREADY_SWITCH_TRADESTATUS(HttpStatus.BAD_REQUEST, "거래완료상태에서 거래상태를 변경할 수 없습니다."),

    // member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 사용자를 찾을 수 없습니다."),
    MEMBER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "해당 사용자가 이미 존재합니다."),

    // review
    ALREADY_REVIEWED(HttpStatus.BAD_REQUEST, "이미 리뷰를 남긴 사용자입니다."),
    INVALID_REVIEWEE(HttpStatus.BAD_REQUEST, "리뷰 대상이 올바르지 않습니다."),
    INVALID_ROLE(HttpStatus.BAD_REQUEST, "상대방의 역할이 올바르지 않습니다."),

    // s3
    FILE_CONVERTION_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 파일을 전환하는데 실패했습니다."),
    INVALID_IMAGE_EXCEPTION(HttpStatus.BAD_REQUEST, "변환할 이미지가 존재하지 않습니다."),

    // offer
    EXCEED_OFFER_COUNT(HttpStatus.BAD_REQUEST, "가격제안 횟수를 초과했습니다."),
    OFFER_NOT_FOUND(HttpStatus.NOT_FOUND, "가격 제안이 존재하지 않습니다."),
    EXISTS_ALREADY_SELECTED_OFFER(HttpStatus.BAD_REQUEST, "이미 선택된 offer가 존재합니다."),
    NOT_SELECTED_OFFER(HttpStatus.BAD_REQUEST, "선택된 Offer가 아닙니다."),

    ;

    private final HttpStatus status;
    private final String message;

}
