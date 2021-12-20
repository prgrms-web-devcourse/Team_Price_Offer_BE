package com.prgrms.offer.domain.article.repository;

import com.prgrms.offer.domain.article.model.entity.Article;
import com.prgrms.offer.domain.search.model.dto.SearchFilterRequest;
import java.util.List;
import org.springframework.data.domain.Pageable;


public interface CustomizedArticleRepository {

    List<Article> findByOnSaleOrBookedInAndFilter(SearchFilterRequest searchFilterRequest, Pageable pageable);

    Long countAllByOnSaleOrBookedInAndFilter(SearchFilterRequest searchFilterRequest);
}
