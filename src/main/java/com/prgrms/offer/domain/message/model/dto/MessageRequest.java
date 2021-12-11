package com.prgrms.offer.domain.message.model.dto;

import com.prgrms.offer.common.message.DtoValidationMessage;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class MessageRequest {

    @Size(min = 1, max = 4000, message = DtoValidationMessage.INVALID_CONTENT_LENGTH)
    private String content;

    public String getContent() {
        return content;
    }
}
