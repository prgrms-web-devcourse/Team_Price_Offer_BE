package com.prgrms.offer.domain.article.model.dto;

import com.prgrms.offer.common.message.DtoValidationMessage;
import io.swagger.annotations.ApiModelProperty;
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

    @Size(min = 1, max = 3, message = DtoValidationMessage.EMPTY_IMAGE_URL)
    private List<String> imageUrls;

    @ApiModelProperty(example = "2")
    @Min(value = 1, message = DtoValidationMessage.INVALID_CODE)
    private int categoryCode;

    @ApiModelProperty(example = "'서울시 강남구'")
    @NotBlank(message = DtoValidationMessage.INVALID_TRADE_AREA)
    private String tradeArea;

    @ApiModelProperty(example = "2")
    @Min(value = 1, message = DtoValidationMessage.INVALID_CODE)
    private int productStatusCode;

    @ApiModelProperty(example = "2")
    @Min(value = 1, message = DtoValidationMessage.INVALID_CODE)
    private int tradeMethodCode;

    @ApiModelProperty(example = "3")
    @Min(value = 1, message = DtoValidationMessage.INVALID_CODE)
    private int quantity;

    @ApiModelProperty(example = "36500")
    @Min(value = 1, message = DtoValidationMessage.INVALID_PRICE)
    private int price;

    @Size(min = 1, max = 4000, message = DtoValidationMessage.INVALID_CONTENT_LENGTH)
    private String content;

}
