package com.prgrms.offer.domain.search.service;

import com.prgrms.offer.core.jwt.JwtAuthentication;
import com.prgrms.offer.domain.article.model.dto.ArticleBriefViewResponse;
import com.prgrms.offer.domain.article.repository.ArticleRepository;
import com.prgrms.offer.domain.article.service.ArticleConverter;
import com.prgrms.offer.domain.member.model.entity.Member;
import com.prgrms.offer.domain.member.repository.MemberRepository;
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
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Page<ArticleBriefViewResponse> findByTitle(
        String title,
        Pageable pageable,
        JwtAuthentication authentication) {

        Member currentMember;

        if (authentication == null) {
            currentMember = memberRepository.findByPrincipal(authentication.loginId).get();

            Page<ArticleBriefViewResponse> articleBriefViewResponses = articleRepository.findByTitleIgnoreCaseContains(
                title, pageable).map(
                article -> articleConverter.toArticleBriefViewResponse(article, false)
            );
            return articleBriefViewResponses;
        }

        // Todo : 인증된 유저 - 좋아요한 게시물 정보 표시
        Page<ArticleBriefViewResponse> titles = articleRepository.findByTitleIgnoreCaseContains(
            title, pageable).map(
            article -> articleConverter.toArticleBriefViewResponse(article, false)
        );
        return titles;
    }

}
