package com.prgrms.offer.domain.article.repository;

import static com.prgrms.offer.domain.article.model.entity.QArticle.article;

import com.prgrms.offer.domain.article.model.entity.Article;
import com.prgrms.offer.domain.article.model.value.TradeStatus;
import com.prgrms.offer.domain.search.model.dto.SearchFilterRequest;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.domain.Pageable;

public class CustomizedArticleRepositoryImpl implements CustomizedArticleRepository {

    private static final List<Integer> tradeStatusOnSaleOrBooked =
        Arrays.asList(TradeStatus.ON_SALE.getCode(), TradeStatus.RESERVING.getCode());

    private final JPAQueryFactory jpaQueryFactory;

    public CustomizedArticleRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<Article> findByOnSaleOrBookedInAndFilter(
        SearchFilterRequest searchFilterRequest,
        Pageable pageable) {

        return jpaQueryFactory.selectFrom(article)
            .where(
                onSaleOrBooked(),
                containsIgnoreTitle(searchFilterRequest.getTitle()),
                eqCategoryCode(searchFilterRequest.getCategoryCode()),
                eqTradeMethodCode(searchFilterRequest.getTradeMethodCode()),
                priceInRange(searchFilterRequest.getMinPrice(), searchFilterRequest.getMaxPrice()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }

    @Override
    public Long countAllByOnSaleOrBookedInAndFilter(
        SearchFilterRequest searchFilterRequest) {
        return jpaQueryFactory.selectFrom(article)
            .where(
                onSaleOrBooked(),
                containsIgnoreTitle(searchFilterRequest.getTitle()),
                eqCategoryCode(searchFilterRequest.getCategoryCode()),
                eqTradeMethodCode(searchFilterRequest.getTradeMethodCode()),
                priceInRange(searchFilterRequest.getMinPrice(), searchFilterRequest.getMaxPrice())
            )
            .fetchCount();
    }

    private BooleanExpression onSaleOrBooked() {
        return article.tradeStatusCode.in(tradeStatusOnSaleOrBooked);
    }

    private BooleanExpression containsIgnoreTitle(String title) {
        return title == null ? null : article.title.containsIgnoreCase(title);
    }

    private BooleanExpression eqCategoryCode(Integer categoryCode) {
        return categoryCode == null ? null : article.categoryCode.eq(categoryCode);
    }

    private BooleanExpression eqTradeMethodCode(Integer tradeMethodCode) {
        return tradeMethodCode == null ? null : article.tradeMethodCode.eq(tradeMethodCode);
    }

    private BooleanExpression priceInRange(Integer minPrice, Integer maxPrice) {
        if (minPrice != null) {
            if (maxPrice != null) {
                return article.price.between(minPrice, maxPrice);
            }
            return article.price.goe(minPrice);
        }

        if (maxPrice != null) {
            return article.price.loe(maxPrice);
        }
        return null;
    }

}
