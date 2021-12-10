package com.prgrms.offer.domain.article.model.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CodeAndNameInfosResponse {
    private List<CodeAndName> categories = new ArrayList<>();

    private List<CodeAndName> productStatus = new ArrayList<>();

    private List<CodeAndName> tradeMethod = new ArrayList<>();

    private List<CodeAndName> tradeStatus = new ArrayList<>();
}
