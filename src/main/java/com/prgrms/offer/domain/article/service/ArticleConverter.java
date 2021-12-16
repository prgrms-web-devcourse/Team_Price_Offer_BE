package com.prgrms.offer.domain.article.service;

import com.prgrms.offer.domain.article.model.dto.*;
import com.prgrms.offer.domain.article.model.entity.Article;
import com.prgrms.offer.domain.article.model.value.Category;
import com.prgrms.offer.domain.article.model.value.ProductStatus;
import com.prgrms.offer.domain.article.model.value.TradeMethod;
import com.prgrms.offer.domain.article.model.value.TradeStatus;
import com.prgrms.offer.domain.member.model.entity.Member;
import com.prgrms.offer.domain.offer.model.entity.Offer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ArticleConverter {
    public ArticleBriefViewResponse toArticleBriefViewResponse(Article article, boolean isLiked){
        return ArticleBriefViewResponse.builder()
                .id(article.getId())
                .mainImageUrl(article.getMainImageUrl())
                .title(article.getTitle())
                .price(article.getPrice())
                .tradeArea(article.getTradeArea())
                .createdDate(article.getCreatedDate())
                .modifiedDate(article.getModifiedDate())
                .isLiked(isLiked)
                .tradeStatus(
                        new CodeAndName(
                                TradeStatus.of(article.getTradeStatusCode()).getCode(),
                                TradeStatus.of(article.getTradeStatusCode()).getName()
                        )
                )
                .build();
    }

    public CodeAndNameInfosResponse toCodeAndNameInfosResponse() {
        var response = new CodeAndNameInfosResponse();

        for(var category : Category.getAllCategory()){
            var codeAndName = new CodeAndName(category.getCode(), category.getName());
            response.getCategories().add(codeAndName);
        }

        for(var productStatus : ProductStatus.getAllProductStatus()){
            var codeAndName = new CodeAndName(productStatus.getCode(), productStatus.getName());
            response.getProductStatus().add(codeAndName);
        }

        for(var tradeMethod : TradeMethod.getAllTradeMethod()){
            var codeAndName = new CodeAndName(tradeMethod.getCode(), tradeMethod.getName());
            response.getTradeMethod().add(codeAndName);
        }

        for(var tradeStatus : TradeStatus.getAllTradeStatus()){
            var codeAndName = new CodeAndName(tradeStatus.getCode(), tradeStatus.getName());
            response.getTradeStatus().add(codeAndName);
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
                        .offerLevel(writer.getOfferLevel())
                        .nickname(writer.getNickname())
                        .profileImageUrl(writer.getProfileImageUrl())
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
                .productStatus(
                        new CodeAndName(
                                ProductStatus.of(article.getProductStatusCode()).getCode(),
                                ProductStatus.of(article.getProductStatusCode()).getName()
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
                .likeCount(article.getLikeCount())
                .isLiked(isLiked)
                .viewCount(article.getViewCount())
                .build();

        return new ArticleDetailResponse(articleDto);
    }

    public ArticleWithOfferBriefViewResponse toArticleWithOfferBriefViewResponse(Article article, boolean isLiked, Offer offer) {
        return ArticleWithOfferBriefViewResponse.builder()
                .id(article.getId())
                .mainImageUrl(article.getMainImageUrl())
                .title(article.getTitle())
                .price(article.getPrice())
                .tradeArea(article.getTradeArea())
                .createdDate(article.getCreatedDate())
                .modifiedDate(article.getModifiedDate())
                .isLiked(isLiked)
                .tradeStatus(
                        new CodeAndName(
                                TradeStatus.of(article.getTradeStatusCode()).getCode(),
                                TradeStatus.of(article.getTradeStatusCode()).getName()
                        )
                )
                .offer(
                        new ArticleWithOfferBriefViewResponse.OfferDto(
                                offer.getPrice(), offer.getIsSelected(), offer.getCreatedDate()
                        )
                )
                .build();
    }
}
