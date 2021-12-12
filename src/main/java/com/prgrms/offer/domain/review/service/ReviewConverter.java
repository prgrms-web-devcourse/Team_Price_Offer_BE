package com.prgrms.offer.domain.review.service;

import com.prgrms.offer.domain.article.model.entity.Article;
import com.prgrms.offer.domain.member.model.entity.Member;
import com.prgrms.offer.domain.review.model.dto.ReviewCreateResponse;
import com.prgrms.offer.domain.review.model.entity.Review;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReviewConverter {
    public Review toEntity(Member reviewee, Member reviewer, Article article, int score, String content) {
        return Review.builder()
                .reviewee(reviewee)
                .reviewer(reviewer)
                .article(article)
                .score(score)
                .content(content)
                .createdDate(LocalDateTime.now())
                .build();
    }

    public ReviewCreateResponse toReviewCreateResponse(Review reviewEntity) {
        return new ReviewCreateResponse(reviewEntity.getId(), reviewEntity.getCreatedDate());
    }
}
