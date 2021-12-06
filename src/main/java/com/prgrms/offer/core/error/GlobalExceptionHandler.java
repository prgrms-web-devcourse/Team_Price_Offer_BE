package com.prgrms.offer.core.error;

import com.prgrms.offer.common.ApiResponse;
import com.prgrms.offer.common.message.ResponseMessage;
import com.prgrms.offer.core.error.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * @Validated 에서 검증 실패시 예외 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse handleMethodValidException(MethodArgumentNotValidException exception){
        return createApiExceptionResult(HttpStatus.BAD_REQUEST, exception.getAllErrors().get(0).getDefaultMessage());
    }

    /**
     * @Valid 에서 검증 실패시 예외 처
     */
    @ExceptionHandler(BindException.class)
    public ApiResponse handleBindException(BindException exception){
        return createApiExceptionResult(HttpStatus.BAD_REQUEST, exception.getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ApiResponse> handleBusinessException(BusinessException e) {
        final ResponseMessage responseMessage = e.getResponseMessage();
        final var apiResponse = ApiResponse.of(responseMessage);
        return ResponseEntity.ok(apiResponse);
    }

    private ApiResponse createApiExceptionResult(HttpStatus httpStatus, String message) {
        return ApiResponse.of(httpStatus, message);
    }
}
