package com.prgrms.offer.domain.article.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CodeAndName {
    private final int code;

    private final String name;
}
