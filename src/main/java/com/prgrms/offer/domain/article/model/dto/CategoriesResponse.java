package com.prgrms.offer.domain.article.model.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CategoriesResponse {
    private List<String> categories;

    public CategoriesResponse(List<String> categories) {
        this.categories = categories;
    }
}
