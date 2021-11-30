package com.prgrms.offer.domain.article.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ArticleBriefViewResponse {
    private Long articleId;

    private String mainImageUrl;

    private String title;

    private int price;

    private String tradeArea;

    private String status;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;
}
