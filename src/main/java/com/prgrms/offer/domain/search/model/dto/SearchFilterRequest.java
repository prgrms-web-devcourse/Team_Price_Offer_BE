package com.prgrms.offer.domain.search.model.dto;

import lombok.Getter;

@Getter
public class SearchFilterRequest {

    private String title;
    private Integer category;
    private Integer tradeMethod;
    private Integer minPrice;
    private Integer maxPrice;

}
