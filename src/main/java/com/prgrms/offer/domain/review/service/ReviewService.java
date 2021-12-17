package com.prgrms.offer.domain.review.service;

import com.prgrms.offer.common.message.ResponseMessage;
import com.prgrms.offer.core.error.exception.BusinessException;
import com.prgrms.offer.core.jwt.JwtAuthentication;
import com.prgrms.offer.domain.article.model.entity.Article;
import com.prgrms.offer.domain.article.repository.ArticleRepository;
import com.prgrms.offer.domain.member.model.entity.Member;
import com.prgrms.offer.domain.member.repository.MemberRepository;
import com.prgrms.offer.domain.offer.model.entity.Offer;
import com.prgrms.offer.domain.offer.repository.OfferRepository;
import com.prgrms.offer.domain.review.model.dto.ReviewCreateRequest;
import com.prgrms.offer.domain.review.model.dto.ReviewCreateResponse;
import com.prgrms.offer.domain.review.model.dto.ReviewResponse;
import com.prgrms.offer.domain.review.model.entity.Review;
import com.prgrms.offer.domain.review.model.value.OfferLevel;
import com.prgrms.offer.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OfferRepository offerRepository;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;

    private final ReviewConverter converter;

    private final String BUYER = "buyer";
    private final String SELLER = "seller";

    @Transactional
    public ReviewCreateResponse createReview(Long articleId, ReviewCreateRequest request, JwtAuthentication authentication) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BusinessException(ResponseMessage.ARTICLE_NOT_FOUND));

        Member reviewer = memberRepository.findByPrincipal(authentication.loginId)
                .orElseThrow(() -> new BusinessException(ResponseMessage.MEMBER_NOT_FOUND));

        if (reviewRepository.existsByReviewerAndArticle(reviewer, article)) {
            throw new BusinessException(ResponseMessage.ALREADY_REVIEWED);
        }

        Offer offer = offerRepository.findByArticleAndIsSelected(article, true)
                .orElseThrow(() -> new BusinessException(ResponseMessage.NOT_SELECTED_OFFER));

        boolean isRevieweeBuyer = article.validateWriterByWriterId(reviewer.getId());

        if(!isRevieweeBuyer){
            if(offer.getOfferer().getId().longValue() != reviewer.getId().longValue()){
                throw new BusinessException(ResponseMessage.PERMISSION_DENIED);
            }
        }

        return isRevieweeBuyer ?
                writeReviewAndGetResponse(reviewer, offer.getOfferer(), article, request, true) :
                writeReviewAndGetResponse(reviewer, article.getWriter(), article, request, false);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    ReviewCreateResponse writeReviewAndGetResponse(Member reviewer, Member reviewee, Article article, ReviewCreateRequest request, boolean isRevieweeBuyer) {
        updateOfferScore(reviewee, request.getScore());

        Review review = converter.toEntity(reviewee, reviewer, article, request.getScore(), request.getContent(), isRevieweeBuyer);
        Review reviewEntity = reviewRepository.save(review);

        return converter.toReviewCreateResponse(reviewEntity);
    }

    @Transactional(readOnly = true)  //memberId 랑 authenticationOptional 가 같은 사용자임(현재 프로필 대상)
    public Page<ReviewResponse> findAllByRole(Pageable pageable, Long memberId, String role, Optional<JwtAuthentication> authenticationOptional) {
        boolean isRevieweeBuyer = getRevieweeRoleIsBuyerOrElseThrow(role);

        Page<Review> reviewPage = reviewRepository.findAllByRevieweeIdAndIsRevieweeBuyer(pageable, memberId, isRevieweeBuyer);

        if (authenticationOptional.isEmpty()) { // 로그인 안한 경우
            return reviewPage.map(r -> converter.toReviewResponse(r, false));
        }

        return reviewPage.map(r -> createReviewResponseForLoginMember(r, authenticationOptional.get()));
    }

    @Transactional(propagation = Propagation.MANDATORY)
    ReviewResponse createReviewResponseForLoginMember(Review review, JwtAuthentication authentication) {
        Member reviewer = memberRepository.findByPrincipal(authentication.loginId)
                .orElseThrow(() -> new BusinessException(ResponseMessage.MEMBER_NOT_FOUND));

        boolean isWritingAvailableFromCurrentMember = !reviewRepository.existsByReviewerAndArticle(reviewer, review.getArticle());

        return converter.toReviewResponse(review, isWritingAvailableFromCurrentMember);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    void updateOfferScore(Member reviewee, int curScore) {
        int score = reviewee.evaluateScore(curScore);
        OfferLevel offerLevel = OfferLevel.calculateOfferLevel(score);
        reviewee.chageOfferLevel(offerLevel.getLevel());
    }

    @Transactional(readOnly = true)
    public ReviewResponse findByArticleIdAndReviewerAuth(Long articleId, JwtAuthentication authentication) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BusinessException(ResponseMessage.ARTICLE_NOT_FOUND));

        Member reviewer = memberRepository.findByPrincipal(authentication.loginId)
                .orElseThrow(() -> new BusinessException(ResponseMessage.MEMBER_NOT_FOUND));

        Review review = reviewRepository.findByReviewerAndArticle(reviewer, article)
                .orElseThrow(() -> new BusinessException(ResponseMessage.REVIEW_NOT_FOUND));

        return converter.toReviewResponse(review, null);
    }

    private boolean getRevieweeRoleIsBuyerOrElseThrow(String role) {
        if (role.equals(BUYER)) {
            return true;
        } else if (role.equals(SELLER)) {
            return false;
        } else {
            throw new BusinessException(ResponseMessage.INVALID_ROLE);
        }
    }
}
