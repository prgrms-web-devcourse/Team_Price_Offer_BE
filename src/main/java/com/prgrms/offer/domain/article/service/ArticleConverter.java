package com.prgrms.offer.domain.article.service;

import com.prgrms.offer.domain.article.model.dto.*;
import com.prgrms.offer.domain.article.model.entity.Article;
import com.prgrms.offer.domain.article.model.value.Category;
import com.prgrms.offer.domain.article.model.value.ProductStatus;
import com.prgrms.offer.domain.article.model.value.TradeMethod;
import com.prgrms.offer.domain.article.model.value.TradeStatus;
import com.prgrms.offer.domain.member.model.entity.Member;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ArticleConverter {
    public ArticleBriefViewResponse toArticleBriefViewResponse(Article article, boolean isLiked){
        return ArticleBriefViewResponse.builder()
                .articleId(article.getId())
                .mainImageUrl(article.getMainImageUrl())
                .title(article.getTitle())
                .price(article.getPrice())
                .tradeArea(article.getTradeArea())
                .createdDate(article.getCreatedDate())
                .modifiedDate(article.getModifiedDate())
                .isLiked(isLiked)
                .build();
    }

    public CategoriesResponse toCategoriesResponse() {
        var response = new CategoriesResponse();

        for(var category : Category.getAllCategory()){
            var categories = new CodeAndName(category.getCode(), category.getName());
            response.getCategories().add(categories);
        }

        return response;
    }

    public Article toEntity(ArticleCreateOrUpdateRequest request, Member writer) {
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
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .viewCount(0)
                .build();
    }

    public ArticleCreateOrUpdateResponse toArticleCreateOrUpdateResponse(Article article) {
        return new ArticleCreateOrUpdateResponse(
                article.getId(),
                article.getCreatedDate(),
                article.getModifiedDate()
        );
    }

    public ArticleDetailResponse toArticleDetailResponse(Article article, boolean isLiked) {
        Member writer = article.getWriter();

        var articleDto = ArticleDetailResponse.ArticleDto.builder()
                .id(article.getId())
                .author(
                        ArticleDetailResponse.AuthorDetail.builder()
                        .id(writer.getId())
                        .email(writer.getAddress())
                        .appleLevel(writer.getAppleLevel())
                        .nickname(writer.getNickname())
                        .profileImageUrl(writer.getProfileImage())
                        .address(writer.getAddress())
                        .build()
                )
                .title(article.getTitle())
                .content(article.getContent())
                .category(
                        new CodeAndName(
                                Category.of(article.getCategoryCode()).getCode(),
                                Category.of(article.getCategoryCode()).getName()
                        )
                )
                .tradeStatus(
                        new CodeAndName(
                                TradeStatus.of(article.getTradeStatusCode()).getCode(),
                                TradeStatus.of(article.getTradeStatusCode()).getName()
                        )
                )
                .tradeArea(article.getTradeArea())
                .tradeMethod(
                        new CodeAndName(
                                TradeMethod.of(article.getTradeMethodCode()).getCode(),
                                TradeMethod.of(article.getTradeMethodCode()).getName()
                        )
                )
                .quantity(article.getQuantity())
                .price(article.getPrice())
                .mainImageUrl(article.getMainImageUrl())
                .createdDate(article.getCreatedDate())
                .modifiedDate(article.getModifiedDate())
                .likeCounts(article.getLikeCount())
                .isLiked(isLiked)
                .viewCount(article.getViewCount())
                .build();

        return new ArticleDetailResponse(articleDto);
    }
}
