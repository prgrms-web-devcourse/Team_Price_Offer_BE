package com.prgrms.offer.domain.article.model.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ProductImageUrlsResponse {
    private final List<String> imageUrls = new ArrayList<>();
}
