package com.prgrms.offer.domain.review.service;

import com.prgrms.offer.common.message.ResponseMessage;
import com.prgrms.offer.core.error.exception.BusinessException;
import com.prgrms.offer.core.jwt.JwtAuthentication;
import com.prgrms.offer.domain.article.model.entity.Article;
import com.prgrms.offer.domain.article.model.value.TradeStatus;
import com.prgrms.offer.domain.article.repository.ArticleRepository;
import com.prgrms.offer.domain.member.model.entity.Member;
import com.prgrms.offer.domain.member.repository.MemberRepository;
import com.prgrms.offer.domain.offer.model.entity.Offer;
import com.prgrms.offer.domain.offer.repository.OfferRepository;
import com.prgrms.offer.domain.review.model.dto.ReviewCreateRequest;
import com.prgrms.offer.domain.review.model.dto.ReviewCreateResponse;
import com.prgrms.offer.domain.review.model.entity.Review;
import com.prgrms.offer.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OfferRepository offerRepository;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;

    private final ReviewConverter converter;

    @Transactional
    public ReviewCreateResponse createReviewToBuyer(Long offerId, Long toMemberId, ReviewCreateRequest request, JwtAuthentication authentication){
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new BusinessException(ResponseMessage.OFFER_NOT_FOUND));

        if(!offer.getIsSelected()){
            throw new BusinessException(ResponseMessage.NOT_SELECTED_OFFER);
        }

        if(!TradeStatus.isCompleted(offer.getArticle().getTradeStatusCode())){
            throw new BusinessException(ResponseMessage.NOT_COMPLETED_TRADE);
        }

        Member fromMember = memberRepository.findByPrincipal(authentication.loginId)
                .orElseThrow(() -> new BusinessException(ResponseMessage.MEMBER_NOT_FOUND));

        if(fromMember.getId().longValue() == toMemberId.longValue()){
            throw new BusinessException(ResponseMessage.INVALID_REVIEWEE);
        }

        if(reviewRepository.existsByFromMemberAndArticle(fromMember, offer.getArticle())){
            throw new BusinessException(ResponseMessage.ALREADY_REVIEWED);
        }

        Member toMember = memberRepository.findById(toMemberId)
                .orElseThrow(() -> new BusinessException(ResponseMessage.MEMBER_NOT_FOUND));

        if(toMember.getId().longValue() == fromMember.getId().longValue()){
            throw new BusinessException(ResponseMessage.PERMISSION_DENIED);
        }

        Review review = converter.toEntity(toMember, fromMember, offer.getArticle(), request.getScore(), request.getContent());
        Review reviewEntity = reviewRepository.save(review);

        return converter.toReviewCreateResponse(reviewEntity);
    }

    @Transactional
    public ReviewCreateResponse createReviewToSeller(Long articleId, Long toMemberId, ReviewCreateRequest request, JwtAuthentication authentication) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BusinessException(ResponseMessage.ARTICLE_NOT_FOUND));

        if(!article.validateWriterByWriterId(toMemberId)){
            throw new BusinessException(ResponseMessage.INVALID_REVIEWEE);
        }

        if(!TradeStatus.isCompleted(article.getTradeStatusCode())){
            throw new BusinessException(ResponseMessage.NOT_COMPLETED_TRADE);
        }

        Member fromMember = memberRepository.findByPrincipal(authentication.loginId)
                .orElseThrow(() -> new BusinessException(ResponseMessage.MEMBER_NOT_FOUND));

        if(fromMember.getId().longValue() == toMemberId.longValue()){
            throw new BusinessException(ResponseMessage.INVALID_REVIEWEE);
        }

        if(reviewRepository.existsByFromMemberAndArticle(fromMember, article)){
            throw new BusinessException(ResponseMessage.ALREADY_REVIEWED);
        }

        Member toMember = memberRepository.findById(toMemberId)
                .orElseThrow(() -> new BusinessException(ResponseMessage.MEMBER_NOT_FOUND));

        Review review = converter.toEntity(toMember, fromMember, article, request.getScore(), request.getContent());
        Review reviewEntity = reviewRepository.save(review);

        return converter.toReviewCreateResponse(reviewEntity);
    }
}
