package com.prgrms.offer.domain.article.repository;

import static com.prgrms.offer.domain.article.model.entity.QArticle.article;

import com.prgrms.offer.domain.article.model.entity.Article;
import com.prgrms.offer.domain.search.model.dto.SearchFilterRequest;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.data.domain.Pageable;

public class CustomizedArticleRepositoryImpl implements CustomizedArticleRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomizedArticleRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<Article> findByFilter(SearchFilterRequest searchFilterRequest, Pageable pageable) {

        return jpaQueryFactory.selectFrom(article)
            .where(containsIgnoreTitle(searchFilterRequest.getTitle()),
                eqCategory(searchFilterRequest.getCategory()),
                eqTradeMethod(searchFilterRequest.getTradeMethod()),
                priceInRange(searchFilterRequest.getMinPrice(), searchFilterRequest.getMaxPrice()))
            .fetch();
    }

    @Override
    public Long countAllByFilter(SearchFilterRequest searchFilterRequest) {
        return jpaQueryFactory.selectFrom(article)
            .where(containsIgnoreTitle(searchFilterRequest.getTitle()),
                eqCategory(searchFilterRequest.getCategory())).fetchCount();
    }

    private BooleanExpression containsIgnoreTitle(String title) {
        return title == null ? null : article.title.containsIgnoreCase(title);
    }

    private BooleanExpression eqCategory(Integer categoryCode) {
        return categoryCode == null ? null : article.categoryCode.eq(categoryCode);
    }

    private BooleanExpression eqTradeMethod(Integer tradeMethod) {
        return tradeMethod == null ? null : article.tradeMethodCode.eq(tradeMethod);
    }

    private BooleanExpression priceInRange(Integer minPrice, Integer maxPrice) {
        if (minPrice != null) {
            if (maxPrice != null) {
                return article.price.between(minPrice, maxPrice);
            }
            return article.price.goe(maxPrice);
        }

        if (maxPrice != null) {
            return article.price.loe(maxPrice);
        }
        return null;
    }
}
