package com.prgrms.offer.domain.article.repository;

import com.prgrms.offer.domain.article.model.entity.Article;
import com.prgrms.offer.domain.article.model.entity.LikeArticle;
import com.prgrms.offer.domain.member.model.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeArticleRepository extends JpaRepository<LikeArticle, Long> {
    boolean existsByMemberAndArticle(Member member, Article article);

    void deleteByMemberIdAndArticleId(Long memberId, Long articleId);

    long countLikeArticlesByMember(Member member);
}
