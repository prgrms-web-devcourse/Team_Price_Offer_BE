package com.prgrms.offer.domain.offer.service;

import com.prgrms.offer.common.message.ResponseMessage;
import com.prgrms.offer.core.error.exception.BusinessException;
import com.prgrms.offer.core.jwt.JwtAuthentication;
import com.prgrms.offer.domain.article.model.entity.Article;
import com.prgrms.offer.domain.article.repository.ArticleRepository;
import com.prgrms.offer.domain.member.model.entity.Member;
import com.prgrms.offer.domain.member.repository.MemberRepository;
import com.prgrms.offer.domain.offer.model.dto.OfferCreateRequest;
import com.prgrms.offer.domain.offer.model.dto.OfferResponse;
import com.prgrms.offer.domain.offer.model.entity.Offer;
import com.prgrms.offer.domain.offer.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OfferService {

    private final OfferRepository offerRepository;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;

    private final OfferConverter converter;

    private final int MAX_AVAIL_OFFER_COUNT = 2;

    @Transactional
    public OfferResponse offer(OfferCreateRequest request, Long articleId, JwtAuthentication authentication) {
        Member offerer = memberRepository.findByPrincipal(authentication.loginId)
                .orElseThrow(() -> new BusinessException(ResponseMessage.MEMBER_NOT_FOUND));

        if ((int) offerRepository.countByOffererIdAndArticleId(offerer.getId(), articleId).longValue() >= MAX_AVAIL_OFFER_COUNT) {
            throw new BusinessException(ResponseMessage.EXCEED_OFFER_COUNT);
        }

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BusinessException(ResponseMessage.ARTICLE_NOT_FOUND));

        Offer offer = converter.toEntity(article, offerer, request.getPrice());
        Offer offerEntity = offerRepository.save(offer);

        return converter.toOfferResponse(offerEntity);
    }

    @Transactional(readOnly = true)
    public Page<OfferResponse> findAllByArticleId(Pageable pageable, Long articleId) {
        Page<Offer> offerPages = offerRepository.findAllByArticleId(pageable, articleId);

        return offerPages.map(o -> converter.toOfferResponse(o));
    }
}
