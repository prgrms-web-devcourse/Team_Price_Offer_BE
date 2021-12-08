package com.prgrms.offer.domain.article.service;

import com.prgrms.offer.common.message.ResponseMessage;
import com.prgrms.offer.common.utils.S3ImageUploader;
import com.prgrms.offer.core.error.exception.BusinessException;
import com.prgrms.offer.core.jwt.JwtAuthentication;
import com.prgrms.offer.domain.article.model.dto.*;
import com.prgrms.offer.domain.article.model.entity.Article;
import com.prgrms.offer.domain.article.model.entity.ProductImage;
import com.prgrms.offer.domain.article.model.value.TradeStatus;
import com.prgrms.offer.domain.article.repository.ArticleRepository;
import com.prgrms.offer.domain.article.repository.LikeArticleRepository;
import com.prgrms.offer.domain.article.repository.ProductImageRepository;
import com.prgrms.offer.domain.member.model.entity.Member;
import com.prgrms.offer.domain.member.model.entity.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final ProductImageRepository productImageRepository;
    private final LikeArticleRepository likeArticleRepository;
    private final ArticleConverter converter;

    private final S3ImageUploader s3ImageUploader;

    private final String PRODUCT_IMAGE_DIR = "productImage";
    private final int MAX_IMAGE_SIZE = 3;

    @Transactional(readOnly = true)
    public Page<ArticleBriefViewResponse> findAllByPages(Pageable pageable, JwtAuthentication authentication) {
        Page<Article> postPage = articleRepository.findAll(pageable);

        final Optional<Member> currentMember = memberRepository.findByPrincipal(authentication.loginId);

        return postPage.map(p -> makeBriefViewResponseWithLikeInfo(p, currentMember));
    }

    public CategoriesResponse findAllCategories() {
        return converter.toCategoriesResponse();
    }

    public List<String> getImageUrls(List<MultipartFile> images) throws IOException {
        List<String> imageUrls = new ArrayList<>();

        for (var image : images) {
            String uploadedImageUrl = s3ImageUploader.upload(image, PRODUCT_IMAGE_DIR);
            imageUrls.add(uploadedImageUrl);
        }

        return imageUrls;
    }

    @Transactional
    public ArticleCreateOrUpdateResponse createOrUpdate(ArticleCreateOrUpdateRequest request, JwtAuthentication authentication) {
        String loginId = authentication.loginId;

        Member writer = memberRepository.findByPrincipal(loginId)
                .orElseThrow(() -> new BusinessException(ResponseMessage.MEMBER_NOT_FOUND));

        Article articleEntity = null;

        if (request.getId() == null || request.getId().longValue() == 0) { // 신규 생성일 경우
            Article article = converter.toEntity(request, writer);
            articleEntity = articleRepository.save(article);
        } else {  // 수정일 경우
            articleEntity = articleRepository.findById(request.getId())
                    .orElseThrow(() -> new BusinessException(ResponseMessage.ARTICLE_NOT_FOUND));

            validateWriterOrElseThrow(articleEntity, loginId);

            articleEntity.updateInfo(
                    request.getTitle(),
                    request.getContent(),
                    request.getCategoryCode(),
                    request.getTradeArea(),
                    request.getQuantity()
            );
            articleEntity.updateMainImageUrl(request.getImageUrls().get(0));

            productImageRepository.deleteAllByArticle(articleEntity);
        }

        saveImagseUrls(articleEntity, request.getImageUrls());

        return converter.toArticleCreateOrUpdateResponse(articleEntity);
    }

    @Transactional
    public void updateTradeStatus(Long articleId, int code, String loginId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BusinessException(ResponseMessage.ARTICLE_NOT_FOUND));

        validateWriterOrElseThrow(article, loginId);

        article.updateTradeStatusCode(TradeStatus.of(code).getCode());
    }

    @Transactional(readOnly = true)
    public ProductImageUrlsResponse findAllImageUrls(Long articleId) {
        if (!articleRepository.existsById(articleId)) {
            throw new BusinessException(ResponseMessage.ARTICLE_NOT_FOUND);
        }

        List<ProductImage> productImages = productImageRepository.findAllByArticleId(articleId);

        var response = new ProductImageUrlsResponse();
        for (var productImage : productImages) {
            response.getImageUrls().add(productImage.getImageUrl());
        }

        int curSize = response.getImageUrls().size();
        for (int i = 1; i <= MAX_IMAGE_SIZE - curSize; i++) {
            response.getImageUrls().add(null);
        }

        return response;
    }

    @Transactional
    public void deleteOne(Long articleId, String loginId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BusinessException(ResponseMessage.ARTICLE_NOT_FOUND));

        validateWriterOrElseThrow(article, loginId);

        articleRepository.delete(article);
    }

    @Transactional(readOnly = true)
    public ArticleDetailResponse findById(Long articleId, JwtAuthentication authentication) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BusinessException(ResponseMessage.ARTICLE_NOT_FOUND));

        final Optional<Member> currentMember = memberRepository.findByPrincipal(authentication.loginId);
        boolean isLiked = false;

        if(currentMember.isPresent()){
            isLiked = likeArticleRepository.existsByMemberAndArticle(currentMember.get(), article);
        }

        return converter.toArticleDetailResponse(article, isLiked);
    }

    private void saveImagseUrls(Article article, List<String> imageUrls){
        for (var imageUrl : imageUrls) {
            if(imageUrl == null || imageUrl.isEmpty()) continue;

            var productImage = new ProductImage(imageUrl, article);
            productImageRepository.save(productImage);
        }
    }

    private void validateWriterOrElseThrow(Article article, String principal){
        if(!article.validateWriterByPrincipal(principal)){
            throw new BusinessException(ResponseMessage.PERMISSION_DENIED);
        }
    }

    private ArticleBriefViewResponse makeBriefViewResponseWithLikeInfo(Article article, Optional<Member> currentMember){
        boolean isLiked = false;

        if(currentMember.isPresent()){
            isLiked = likeArticleRepository.existsByMemberAndArticle(currentMember.get(), article);
        }

        return converter.toArticleBriefViewResponse(article, isLiked);
    }
}
