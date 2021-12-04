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

    // authentication
    LOGIN_FAIL(HttpStatus.BAD_REQUEST, "email 또는 비밀번호가 일치하지 않습니다."),

    // article
    ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 게시글을 찾을 수 없습니다."),
    PRODUCT_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 상품 상태(중고상품, 새상품)가 올바르지 않습니다."),
    TRADE_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 게시글 상태가 존재하지 않습니다."),
    TRADE_METHOD_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 거래방식이 존재하지 않습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리가 존재하지 않습니다."),

    // member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 사용자를 찾을 수 없습니다."),
    MEMBER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "해당 사용자가 이미 존재합니다."),

    // review
    REVIEW_CREATION_FAIL(HttpStatus.BAD_REQUEST, "해당 사용자와 거래한 상품이 없습니다."),

    ;

    private final HttpStatus status;
    private final String message;

}
