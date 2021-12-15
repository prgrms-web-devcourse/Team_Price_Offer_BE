package com.prgrms.offer.domain.review.service;

import com.prgrms.offer.domain.article.model.entity.Article;
import com.prgrms.offer.domain.member.model.entity.Member;
import com.prgrms.offer.domain.review.model.dto.ReviewCreateResponse;
import com.prgrms.offer.domain.review.model.dto.ReviewResponse;
import com.prgrms.offer.domain.review.model.entity.Review;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReviewConverter {
    public Review toEntity(Member reviewee, Member reviewer, Article article, int score, String content, boolean isRevieweeBuyer) {
        return Review.builder()
                .reviewee(reviewee)
                .reviewer(reviewer)
                .article(article)
                .score(score)
                .content(content)
                .isRevieweeBuyer(isRevieweeBuyer)
                .createdDate(LocalDateTime.now())
                .build();
    }

    public ReviewCreateResponse toReviewCreateResponse(Review reviewEntity) {
        return new ReviewCreateResponse(reviewEntity.getId(), reviewEntity.getCreatedDate());
    }

    public ReviewResponse toReviewResponse(Review review, boolean isAvailWriteReviewFromCurrentMember) {
        Article article = review.getArticle();
        Member reviewer = review.getReviewer();

        return ReviewResponse.builder()
                .id(review.getId())
                .reviewer(
                        new ReviewResponse.ReviewerDto(
                                reviewer.getId(), reviewer.getProfileImageUrl(), reviewer.getNickname(), reviewer.getOfferLevel()
                        )
                )
                .score(review.getScore())
                .article(
                        new ReviewResponse.ArticleBriefDto(
                                article.getId(), article.getTitle()
                        )
                )
                .content(review.getContent())
                .isAvailWriteReviewFromCurrentMember(isAvailWriteReviewFromCurrentMember)
                .createdDate(review.getCreatedDate())
                .build();
    }
}
