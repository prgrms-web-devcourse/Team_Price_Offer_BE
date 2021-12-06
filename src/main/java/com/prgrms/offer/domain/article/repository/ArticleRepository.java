package com.prgrms.offer.domain.article.repository;

import com.prgrms.offer.domain.article.model.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Override
    Optional<Article> findById(Long aLong);

    @Override
    Page<Article> findAll(Pageable pageable);

    @Override
    <S extends Article> S save(S entity);
}
