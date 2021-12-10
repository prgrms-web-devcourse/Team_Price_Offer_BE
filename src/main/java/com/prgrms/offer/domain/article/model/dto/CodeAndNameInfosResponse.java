package com.prgrms.offer.domain.article.model.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CodeAndNameInfosResponse {
    private final List<CodeAndName> categories = new ArrayList<>();

    private final List<CodeAndName> productStatus = new ArrayList<>();

    private final List<CodeAndName> tradeMethod = new ArrayList<>();

    private final List<CodeAndName> tradeStatus = new ArrayList<>();
}
