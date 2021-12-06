package com.prgrms.offer.domain.article.model.dto;

import com.prgrms.offer.common.message.DtoValidationMessage;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class TradeStatusUpdateRequest {
    @NotNull(message = DtoValidationMessage.INVALID_CODE)
    private Integer code;
}
