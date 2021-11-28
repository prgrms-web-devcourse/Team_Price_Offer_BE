package com.prgrms.offer.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class ApiResponse<T> {

    private final int code;
    private final String message;

    @JsonInclude
    private final T data;

    public static <T> ApiResponse<T> of(HttpStatus status, String message) {
        int code = Optional.ofNullable(status)
            .orElse(HttpStatus.OK)
            .value();
        return new ApiResponse<>(code, message, null);
    }

    public static <T> ApiResponse<T> of(HttpStatus status, String message, T data) {
        int code = Optional.ofNullable(status)
            .orElse(HttpStatus.OK)
            .value();
        return new ApiResponse<>(code, message, data);
    }
}
