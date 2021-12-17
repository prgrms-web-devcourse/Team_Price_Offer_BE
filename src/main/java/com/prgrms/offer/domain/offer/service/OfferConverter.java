package com.prgrms.offer.domain.offer.service;

import com.prgrms.offer.domain.article.model.entity.Article;
import com.prgrms.offer.domain.member.model.entity.Member;
import com.prgrms.offer.domain.offer.model.dto.OfferBriefResponse;
import com.prgrms.offer.domain.offer.model.dto.OfferResponse;
import com.prgrms.offer.domain.offer.model.dto.OffererResponse;
import com.prgrms.offer.domain.offer.model.entity.Offer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OfferConverter {
    public Offer toEntity(Article article, Member offerer, int price){
        return Offer.builder()
                .article(article)
                .offerer(offerer)
                .price(price)
                .createdDate(LocalDateTime.now())
                .isSelected(false)
                .build();
    }

    public OfferResponse toOfferResponse(Offer offer, Integer offerCountOfCurrentMember) {
        var offerer = offer.getOfferer();

        var response = OfferResponse.OfferDto.builder()
                .id(offer.getId())
                .offerer(
                        new OffererResponse(
                                offerer.getId(),
                                offerer.getNickname(),
                                offerer.getAddress(),
                                offerer.getOfferLevel(),
                                offerer.getProfileImageUrl()
                        )
                )
                .articleId(offer.getArticle().getId())
                .price(offer.getPrice())
                .createdDate(offer.getCreatedDate())
                .isSelected(offer.getIsSelected())
                .build();

        return new OfferResponse(response, offerCountOfCurrentMember);
    }

    public OfferBriefResponse toOfferOfferBriefResponse(Offer offer) {
        var offerer = offer.getOfferer();

        return OfferBriefResponse.builder()
                .id(offer.getId())
                .offerer(
                        new OffererResponse(
                                offerer.getId(),
                                offerer.getNickname(),
                                offerer.getAddress(),
                                offerer.getOfferLevel(),
                                offerer.getProfileImageUrl()
                        )
                )
                .articleId(offer.getArticle().getId())
                .price(offer.getPrice())
                .createdDate(offer.getCreatedDate())
                .isSelected(offer.getIsSelected())
                .build();
    }
}
