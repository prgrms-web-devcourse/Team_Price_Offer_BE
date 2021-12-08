package com.prgrms.offer.domain.search.service;

import com.prgrms.offer.domain.article.model.dto.ArticleBriefViewResponse;
import com.prgrms.offer.domain.article.repository.ArticleRepository;
import com.prgrms.offer.domain.article.service.ArticleConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ArticleSearchService {

    private final ArticleConverter articleConverter;
    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public Page<ArticleBriefViewResponse> findByTitle(String title, Pageable pageable) {
        Page<ArticleBriefViewResponse> titles = articleRepository.findByTitleIgnoreCaseContains(
            title, pageable).map(
            article -> articleConverter.toArticleBriefViewResponse(article)
        );
        return titles;
    }

}
