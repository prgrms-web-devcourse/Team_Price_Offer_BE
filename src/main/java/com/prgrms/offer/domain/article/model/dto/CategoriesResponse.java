package com.prgrms.offer.domain.article.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CategoriesResponse {
    private List<Categories> categories = new ArrayList<>();

    @Getter
    @RequiredArgsConstructor
    public static class Categories{
        private final int code;
        private final String name;
    }

}
