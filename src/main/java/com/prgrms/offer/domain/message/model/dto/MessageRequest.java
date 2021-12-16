package com.prgrms.offer.domain.message.model.dto;

import com.prgrms.offer.common.message.DtoValidationMessage;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class MessageRequest {

    @NotNull(message = DtoValidationMessage.IVALID_MESSAGE_LENGTH)
    @Size(min = 1, max = 100, message = DtoValidationMessage.IVALID_MESSAGE_LENGTH)
    private String content;

    public String getContent() {
        return content;
    }
}
