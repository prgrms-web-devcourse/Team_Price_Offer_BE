package com.prgrms.offer.domain.article.repository;

import com.prgrms.offer.domain.article.model.entity.Article;
import com.prgrms.offer.domain.article.model.entity.LikeArticle;
import com.prgrms.offer.domain.member.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LikeArticleRepository extends JpaRepository<LikeArticle, Long> {
    boolean existsByMemberAndArticle(Member member, Article article);

    void deleteByMemberIdAndArticleId(Long memberId, Long articleId);

    long countLikeArticlesByMember(Member member);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE LikeArticle la SET la.article = NULL WHERE la.article = :article")
    void doOnDeleteSetNullFromArticle(Article article);
}
