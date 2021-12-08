package com.prgrms.offer.domain.article.model.dto;

import com.prgrms.offer.common.message.DtoValidationMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ArticleCreateOrUpdateRequest {

    private Long id;

    @NotBlank(message = DtoValidationMessage.INVALID_TITLE_LENGTH)
    private String title;

    private List<String> imageUrls;

    @Min(value = 1, message = DtoValidationMessage.INVALID_CODE)
    private int categoryCode;

    @NotBlank(message = DtoValidationMessage.INVALID_TRADE_AREA)
    private String tradeArea;

    @Min(value = 1, message = DtoValidationMessage.INVALID_CODE)
    private int productStatusCode;

    @Min(value = 1, message = DtoValidationMessage.INVALID_CODE)
    private int tradeMethodCode;

    @Min(value = 1, message = DtoValidationMessage.INVALID_CODE)
    private int quantity;

    @Min(value = 1, message = DtoValidationMessage.INVALID_PRICE)
    private int price;

    @Size(min = 1, max = 4000, message = DtoValidationMessage.INVALID_CONTENT_LENGTH)
    private String content;

}
