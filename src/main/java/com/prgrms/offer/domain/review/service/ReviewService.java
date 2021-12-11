package com.prgrms.offer.domain.review.service;

import com.prgrms.offer.common.message.ResponseMessage;
import com.prgrms.offer.core.error.exception.BusinessException;
import com.prgrms.offer.core.jwt.JwtAuthentication;
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

    private final ReviewConverter converter;

    @Transactional
    public ReviewCreateResponse createReviewToBuyer(Long offerId, Long toMemberId, ReviewCreateRequest request, JwtAuthentication authentication){
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new BusinessException(ResponseMessage.OFFER_NOT_FOUND));

        if(!offer.getIsSelected()){
            throw new BusinessException(ResponseMessage.NOT_SELECTED_OFFER);
        }

        Member fromMember = memberRepository.findByPrincipal(authentication.loginId)
                .orElseThrow(() -> new BusinessException(ResponseMessage.MEMBER_NOT_FOUND));

        if(reviewRepository.existsByFromMemberAndArticle(fromMember, offer.getArticle())){
            throw new BusinessException(ResponseMessage.ALREADY_REVIEWED);
        }

        Member toMember = memberRepository.findById(toMemberId)
                .orElseThrow(() -> new BusinessException(ResponseMessage.MEMBER_NOT_FOUND));

        Review review = converter.toEntity(toMember, fromMember, offer.getArticle(), request.getScore(), request.getContent());
        Review reviewEntity = reviewRepository.save(review);

        return converter.toReviewCreateResponse(reviewEntity);
    }
}
