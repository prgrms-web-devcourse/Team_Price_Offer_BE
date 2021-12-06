package com.prgrms.offer.domain.article.repository;

import com.prgrms.offer.domain.article.model.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    @Override
    <S extends ProductImage> S save(S entity);
}
