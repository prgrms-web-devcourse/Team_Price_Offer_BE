package com.prgrms.offer.domain.search.model.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SearchFilterRequest {

    private String title;
    private Integer categoryCode;
    private Integer tradeMethodCode;
    private Integer minPrice;
    private Integer maxPrice;

}
