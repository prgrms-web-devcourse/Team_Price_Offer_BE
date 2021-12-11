package com.prgrms.offer.domain.review.model.dto;

import com.prgrms.offer.common.message.DtoValidationMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class ReviewCreateRequest {
    @Min(value = -1, message = DtoValidationMessage.INVALID_REVIEW_SCORE)
    @Max(value = 3, message = DtoValidationMessage.INVALID_REVIEW_SCORE)
    private int score;

    @Size(min = 3, max = 4000, message = DtoValidationMessage.INVALID_CONTENT_LENGTH)
    private String content;
}
