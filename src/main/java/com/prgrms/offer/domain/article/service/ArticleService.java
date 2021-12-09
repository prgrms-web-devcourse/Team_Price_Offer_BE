package com.prgrms.offer.domain.article.service;

import com.prgrms.offer.common.message.ResponseMessage;
import com.prgrms.offer.common.utils.S3ImageUploader;
import com.prgrms.offer.core.error.exception.BusinessException;
import com.prgrms.offer.core.jwt.JwtAuthentication;
import com.prgrms.offer.domain.article.model.dto.ArticleBriefViewResponse;
import com.prgrms.offer.domain.article.model.dto.ArticleCreateOrUpdateRequest;
import com.prgrms.offer.domain.article.model.dto.ArticleCreateOrUpdateResponse;
import com.prgrms.offer.domain.article.model.dto.ArticleDetailResponse;
import com.prgrms.offer.domain.article.model.dto.CategoriesResponse;
import com.prgrms.offer.domain.article.model.dto.ProductImageUrlsResponse;
import com.prgrms.offer.domain.article.model.entity.Article;
import com.prgrms.offer.domain.article.model.entity.ProductImage;
import com.prgrms.offer.domain.article.model.value.TradeStatus;
import com.prgrms.offer.domain.article.repository.ArticleRepository;
import com.prgrms.offer.domain.article.repository.LikeArticleRepository;
import com.prgrms.offer.domain.article.repository.ProductImageRepository;
import com.prgrms.offer.domain.member.model.entity.Member;
import com.prgrms.offer.domain.member.repository.MemberRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    public ArticleDetailResponse findById(Long articleId, Optional<JwtAuthentication> authenticationOptional) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BusinessException(ResponseMessage.ARTICLE_NOT_FOUND));

        boolean isLiked = false;
        if (authenticationOptional.isPresent()) {
            Member currentMember = memberRepository.findByPrincipal(authenticationOptional.get().loginId)
                    .orElseThrow(() -> new BusinessException(ResponseMessage.MEMBER_NOT_FOUND));

            isLiked = likeArticleRepository.existsByMemberAndArticle(currentMember, article);
        }

        return converter.toArticleDetailResponse(article, isLiked);
    }

    @Transactional(readOnly = true)
    public Page<ArticleBriefViewResponse> findAllByPages(
            Pageable pageable,
            Optional<JwtAuthentication> authenticationOptional
    ) {

        Page<Article> postPage = articleRepository.findAll(pageable);

        if (!authenticationOptional.isPresent()) {
            return postPage.map(p -> converter.toArticleBriefViewResponse(p, false));
        }

        Member currentMember = memberRepository.findByPrincipal(authenticationOptional.get().loginId)
                .orElseThrow(() -> new BusinessException(ResponseMessage.MEMBER_NOT_FOUND));

        return postPage.map(p -> makeBriefViewResponseWithLikeInfo(p, currentMember));
    }

    private ArticleBriefViewResponse makeBriefViewResponseWithLikeInfo(Article article, Member currentMember) {
        boolean isLiked = likeArticleRepository.existsByMemberAndArticle(currentMember, article);

        return converter.toArticleBriefViewResponse(article, isLiked);
    }

    private void validateWriterOrElseThrow(Article article, String principal) {
        if (!article.validateWriterByPrincipal(principal)) {
            throw new BusinessException(ResponseMessage.PERMISSION_DENIED);
        }
    }

    private void saveImagseUrls(Article article, List<String> imageUrls) {
        for (var imageUrl : imageUrls) {
            if (imageUrl == null || imageUrl.isEmpty()) {
                continue;
            }

            var productImage = new ProductImage(imageUrl, article);
            productImageRepository.save(productImage);
        }
    }

}
