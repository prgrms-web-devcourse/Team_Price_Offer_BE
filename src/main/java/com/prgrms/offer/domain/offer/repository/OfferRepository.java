package com.prgrms.offer.domain.offer.repository;

import com.prgrms.offer.domain.offer.model.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    @Override
    <S extends Offer> S save(S entity);

    Long countByOffererIdAndArticleId(Long offererId, Long articleId);
}
