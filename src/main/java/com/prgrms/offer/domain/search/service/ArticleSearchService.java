package com.prgrms.offer.domain.search.service;

import static com.prgrms.offer.common.page.CollectionToPage.toPage;

import com.prgrms.offer.common.message.ResponseMessage;
import com.prgrms.offer.core.error.exception.BusinessException;
import com.prgrms.offer.core.jwt.JwtAuthentication;
import com.prgrms.offer.domain.article.model.dto.ArticleBriefViewResponse;
import com.prgrms.offer.domain.article.model.entity.Article;
import com.prgrms.offer.domain.article.repository.ArticleRepository;
import com.prgrms.offer.domain.article.repository.LikeArticleRepository;
import com.prgrms.offer.domain.article.service.ArticleConverter;
import com.prgrms.offer.domain.member.model.entity.Member;
import com.prgrms.offer.domain.member.repository.MemberRepository;
import com.prgrms.offer.domain.search.model.dto.SearchFilterRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
    private final LikeArticleRepository likeArticleRepository;
    private final MemberRepository memberRepository;

    private final Integer[] tradeStatusCodeArray = {2, 4};

    @Transactional(readOnly = true)
    public Page<ArticleBriefViewResponse> findByTitle(
        String title,
        Pageable pageable,
        Optional<JwtAuthentication> authentication) {

        if (authentication.isEmpty()) {
            Page<ArticleBriefViewResponse> articleBriefViewResponses = articleRepository.findByTitleIgnoreCaseContainsAndTradeStatusCodeIn(
                title, tradeStatusCodeArray , pageable).map(
                article -> articleConverter.toArticleBriefViewResponse(article, false)
            );
            return articleBriefViewResponses;
        }

        Member currentMember = memberRepository.findByPrincipal(authentication.get().loginId)
            .orElseThrow(() -> new BusinessException(ResponseMessage.MEMBER_NOT_FOUND));

        Page<ArticleBriefViewResponse> titles = articleRepository.findByTitleIgnoreCaseContainsAndTradeStatusCodeIn(
            title, tradeStatusCodeArray, pageable).map(
            article -> makeBriefViewResponseWithLikeInfo(article, currentMember)
        );
        return titles;
    }

    public Page<ArticleBriefViewResponse> findByFilter(
        SearchFilterRequest searchFilterRequest,
        Pageable pageable,
        Optional<JwtAuthentication> authentication) {

        List<Article> articleList = articleRepository.findByTradeStatusCodeInAndFilter(
            tradeStatusCodeArray, searchFilterRequest, pageable);

        long numContents = articleRepository.countAllByTradeStatusCodeInAndFilter(tradeStatusCodeArray, searchFilterRequest);

        if (authentication.isEmpty()) {
            return (Page<ArticleBriefViewResponse>) toPage(articleList.stream().map(
                article -> articleConverter.toArticleBriefViewResponse(article, false)).collect(
                Collectors.toList()), pageable, numContents);
        }

        JwtAuthentication jwtAuthentication = authentication.get();

        Member currentMember = memberRepository.findByPrincipal(jwtAuthentication.loginId)
            .orElseThrow(() -> new BusinessException(ResponseMessage.MEMBER_NOT_FOUND));

        return (Page<ArticleBriefViewResponse>) toPage(articleList.stream().map(
                article -> makeBriefViewResponseWithLikeInfo(article, currentMember))
            .collect(Collectors.toList()), pageable, numContents);
    }

    private ArticleBriefViewResponse makeBriefViewResponseWithLikeInfo(Article article,
        Member currentMember) {
        boolean isLiked = likeArticleRepository.existsByMemberAndArticle(currentMember, article);

        return articleConverter.toArticleBriefViewResponse(article, isLiked);
    }

}
