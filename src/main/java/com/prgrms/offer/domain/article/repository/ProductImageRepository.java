package com.prgrms.offer.domain.article.repository;

import com.prgrms.offer.domain.article.model.entity.Article;
import com.prgrms.offer.domain.article.model.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    @Override
    <S extends ProductImage> S save(S entity);

    void deleteAllByArticle(Article article);

    List<ProductImage> findAllByArticleId(Long articleId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE ProductImage pi SET pi.article = NULL WHERE pi.article = :article")
    void doOnDeleteSetNullFromArticle(Article article);
}
