package com.prgrms.offer.core.error;

import com.prgrms.offer.common.ApiResponse;
import com.prgrms.offer.common.message.ResponseMessage;
import com.prgrms.offer.core.error.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * @Valid 에서 검증 실패시 예외 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse handleMethodValidException(MethodArgumentNotValidException exception){
        log.info(exception.getAllErrors().get(0).getDefaultMessage() + " from MethodArgumentNotValidException");
        return createApiExceptionResult(HttpStatus.BAD_REQUEST, exception.getAllErrors().get(0).getDefaultMessage());
    }

    /**
     * @Validated 에서 검증 실패시 예외 처
     */
    @ExceptionHandler(BindException.class)
    public ApiResponse handleBindException(BindException exception){
        log.info(exception.getAllErrors().get(0).getDefaultMessage() + " from BindException");
        return createApiExceptionResult(HttpStatus.BAD_REQUEST, exception.getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse> handleBusinessException(BusinessException e) {
        log.info(e.getMessage() + " from BusinessException");
        final ResponseMessage responseMessage = e.getResponseMessage();
        final var apiResponse = ApiResponse.of(responseMessage);
        return ResponseEntity.ok(apiResponse);
    }

    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        log.info(exception.getMessage() + " from HttpRequestMethodNotSupportedException");
        return createApiExceptionResult(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    private ApiResponse createApiExceptionResult(HttpStatus httpStatus, String message) {
        return ApiResponse.of(httpStatus, message);
    }
}
