package com.prgrms.offer.domain.article.service;

import com.prgrms.offer.domain.article.model.dto.ArticleBriefViewResponse;
import com.prgrms.offer.domain.article.model.dto.ArticleCreateRequest;
import com.prgrms.offer.domain.article.model.dto.ArticleCreateResponse;
import com.prgrms.offer.domain.article.model.dto.CategoriesResponse;
import com.prgrms.offer.domain.article.model.entity.Article;
import com.prgrms.offer.domain.article.model.value.Category;
import com.prgrms.offer.domain.article.model.value.ProductStatus;
import com.prgrms.offer.domain.article.model.value.TradeMethod;
import com.prgrms.offer.domain.article.model.value.TradeStatus;
import com.prgrms.offer.domain.member.model.entity.Member;
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

    public Article toEntity(ArticleCreateRequest request, Member writer) {
        return Article.builder()
                .writer(writer)
                .likeCount(0)
                .title(request.getTitle())
                .content(request.getContent())
                .categoryCode(Category.of(request.getCategoryCode()).getCode())
                .productStatusCode(ProductStatus.of(request.getProductStatusCode()).getCode())
                .tradeArea(request.getTradeArea())
                .quantity(request.getQuantity())
                .tradeMethodCode(TradeMethod.of(request.getTradeMethodCode()).getCode())
                .tradeStatusCode(TradeStatus.ON_SALE.getCode())
                .mainImageUrl(request.getImageUrls() == null || request.getImageUrls().isEmpty() ? null : request.getImageUrls().get(0))
                .price(request.getPrice())
                .viewCount(0)
                .build();
    }

    public ArticleCreateResponse toArticleCreateResponse(Article articleEntity) {
        return new ArticleCreateResponse(articleEntity.getId(), articleEntity.getCreatedDate());
    }
}
