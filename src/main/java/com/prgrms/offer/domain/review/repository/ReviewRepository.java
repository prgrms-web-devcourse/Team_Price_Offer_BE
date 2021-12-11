package com.prgrms.offer.domain.review.repository;

import com.prgrms.offer.domain.review.model.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
