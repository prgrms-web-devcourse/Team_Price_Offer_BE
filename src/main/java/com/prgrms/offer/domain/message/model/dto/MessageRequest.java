package com.prgrms.offer.domain.message.model.dto;

import javax.validation.constraints.NotBlank;

public class MessageRequest {

    @NotBlank
    private String content;

    public String getContent() {
        return content;
    }
}
