package com.prgrms.offer.core.error.exception;

import com.prgrms.offer.common.message.ResponseMessage;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{

    private ResponseMessage responseMessage;

    public BusinessException(ResponseMessage responseMessage) {
        super(responseMessage.getMessage());
        this.responseMessage = responseMessage;
    }

}
