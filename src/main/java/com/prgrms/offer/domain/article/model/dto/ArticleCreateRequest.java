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
public class ArticleCreateRequest {

    @NotBlank(message = DtoValidationMessage.INVALID_POST_TITLE)
    private String title;

    private List<String> imageUrls;

    private int categoryCode;

    @NotBlank(message = DtoValidationMessage.INVALID_TRADE_AREA)
    private String tradeArea;

    private int productStatusCode;

    private int tradeMethodCode;

    @Min(value = 1)
    private int quantity;

    @Min(value = 1)
    private int price;

    @Size(min = 5, max = 4000)
    @NotBlank(message = DtoValidationMessage.INVALID_TRADE_AREA)
    private String content;

}
