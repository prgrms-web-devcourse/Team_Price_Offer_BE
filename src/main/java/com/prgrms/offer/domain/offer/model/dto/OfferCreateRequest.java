package com.prgrms.offer.domain.offer.model.dto;

import com.prgrms.offer.common.message.DtoValidationMessage;
import lombok.Getter;

import javax.validation.constraints.Min;

@Getter
public class OfferCreateRequest {
    @Min(value = 0, message = DtoValidationMessage.INVALID_OFFER_PRICE)
    private int price;
}
