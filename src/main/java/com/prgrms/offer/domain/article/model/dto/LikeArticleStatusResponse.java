package com.prgrms.offer.domain.article.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LikeArticleStatusResponse {
    private final boolean isLiked;
}
