package com.prgrms.offer.domain.article.service;

import com.prgrms.offer.domain.article.model.dto.ArticleBriefViewResponse;
import com.prgrms.offer.domain.article.model.dto.CategoriesResponse;
import com.prgrms.offer.domain.article.model.entity.Article;
import com.prgrms.offer.domain.article.model.value.Category;
import org.springframework.stereotype.Component;

@Component
public class ArticleConverter {
    public ArticleBriefViewResponse toArticleBriefViewResponse(Article article){
        return ArticleBriefViewResponse.builder()
                .articleId(article.getId())
                .mainImageUrl(article.getMainImageUrl())
                .title(article.getTitle())
                .price(article.getPrice())
                .tradeArea(article.getTradeArea())
                .createdDate(article.getCreatedDate())
                .modifiedDate(article.getModifiedDate())
                .build();
    }

    public CategoriesResponse toCategoriesResponse() {
        return new CategoriesResponse(Category.getAllCategoryName());
    }
}
