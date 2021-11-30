package com.prgrms.offer.domain.article.repository;

import com.prgrms.offer.domain.article.model.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
