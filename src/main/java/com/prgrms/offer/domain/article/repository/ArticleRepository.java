package com.prgrms.offer.domain.article.repository;

import com.prgrms.offer.domain.article.model.entity.Article;
import com.prgrms.offer.domain.member.model.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long>, CustomizedArticleRepository {
    @Override
    Optional<Article> findById(Long aLong);

    @Override
    Page<Article> findAll(Pageable pageable);

    @Override
    <S extends Article> S save(S entity);

    @Override
    boolean existsById(Long aLong);

    Page<Article> findByTitleIgnoreCaseContainsAndTradeStatusCodeIn(String title, Integer[] tradeStatusCodeArray, Pageable pageable);

    Page<Article> findAllByCategoryCode(Pageable pageable, int categoryCode);

    Page<Article> findAllByWriterIdAndTradeStatusCode(Pageable pageable, Long writerId, int tradeStatusCode);

    Page<Article> findAllByTradeStatusCode(Pageable pageable, int tradeStatusCode);

    Page<Article> findAllByWriterId(Pageable pageable, Long writerId);

    long countArticlesByWriter(Member member);

    @Query(value = "select * from article where article_id in (select like_article.article_id from like_article where member_id = ?1)" +
            "and (trade_status_code = 2 or trade_status_code = 4)",
            countQuery = "select count(*) from article where article_id in (select like_article.article_id from like_article where member_id = ?1)" +
                    "and (trade_status_code = 2 or trade_status_code = 4)",
            nativeQuery = true)
    Page<Article> findLikedSellingArticleByMember(long memberId, Pageable pageable);

    @Query(value = "select * from article where article_id in (select like_article.article_id from like_article where member_id = ?1)" +
            "and trade_status_code = 8",
            countQuery = "select count(*) from article where article_id in (select like_article.article_id from like_article where member_id = ?1)" +
                    "and trade_status_code = 8",
            nativeQuery = true)
    Page<Article> findLikedCompletedArticleByMember(long memberId, Pageable pageable);

    @Query("select a from Article a where a.writer.id = :writerId and a.tradeStatusCode <> 8")
    Page<Article> findAllByWriterIdAndTradeInProgress(Pageable pageable, Long writerId);

    @Query("select a from Article a where a.tradeStatusCode <> 8")
    Page<Article> findAllByTradeInProgress(Pageable pageable);

    @Query("select " +
            "new com.prgrms.offer.domain.article.repository.TemporalArticle(max(o.article.id), max(o.article.mainImageUrl), max(o.article.title), max(o.article.price), max(o.article.tradeArea), max(o.article.tradeStatusCode), a.createdDate, max(o.article.modifiedDate)) " +
            "from Article a join Offer o on a.id = o.article.id " +
            "where o.offerer = :offerer and o.article.tradeStatusCode = :tradeStatusCode " +
            "group by a.createdDate")
    Page<TemporalArticle> findAllByOffererAndTradeStatusCode(Member offerer, int tradeStatusCode, Pageable pageable);

    @Query("select " +
            "new com.prgrms.offer.domain.article.repository.TemporalArticle(max(o.article.id), max(o.article.mainImageUrl), max(o.article.title), max(o.article.price), max(o.article.tradeArea), max(o.article.tradeStatusCode), a.createdDate, max(o.article.modifiedDate)) " +
            "from Article a join Offer o on a.id = o.article.id " +
            "where o.offerer = :offerer and o.article.tradeStatusCode <> 8 " +
            "group by a.createdDate")
    Page<TemporalArticle> findAllByOffererAndTradeInProgress(Member offerer, Pageable pageable);
}