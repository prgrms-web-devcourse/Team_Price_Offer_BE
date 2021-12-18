package com.prgrms.offer.domain.search.model.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchFilterRequest {

    private String title;
    private Integer category;
    private Integer tradeMethod;
    private Integer minPrice;
    private Integer maxPrice;

}
