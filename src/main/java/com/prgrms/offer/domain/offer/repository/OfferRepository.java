package com.prgrms.offer.domain.offer.repository;

import com.prgrms.offer.domain.offer.model.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository<Offer, Long> {
}
