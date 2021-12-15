package com.prgrms.offer.domain.review.repository;

import com.prgrms.offer.domain.article.model.entity.Article;
import com.prgrms.offer.domain.member.model.entity.Member;
import com.prgrms.offer.domain.review.model.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Override
    <S extends Review> S save(S entity);

    boolean existsByReviewerAndArticle(Member reviewer, Article article);

    Page<Review> findAllByRevieweeIdAndIsRevieweeBuyer(Pageable pageable, Long revieweeId, boolean isRevieweeBuyer);

    Optional<Review> findByReviewerAndArticle(Member reviewer, Article article);

    long countReviewsByReviewee(Member member);
}
