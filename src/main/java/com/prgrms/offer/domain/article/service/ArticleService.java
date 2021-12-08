package com.prgrms.offer.domain.article.service;

import com.prgrms.offer.common.message.ResponseMessage;
import com.prgrms.offer.common.utils.S3ImageUploader;
import com.prgrms.offer.core.error.exception.BusinessException;
import com.prgrms.offer.domain.article.model.dto.*;
import com.prgrms.offer.domain.article.model.entity.Article;
import com.prgrms.offer.domain.article.model.entity.ProductImage;
import com.prgrms.offer.domain.article.model.value.TradeStatus;
import com.prgrms.offer.domain.article.repository.ArticleRepository;
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

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final ProductImageRepository productImageRepository;
    private final ArticleConverter converter;

    private final S3ImageUploader s3ImageUploader;

    private final String PRODUCT_IMAGE_DIR = "productImage";
    private final int MAX_IMAGE_SIZE = 3;

    @Transactional(readOnly = true)
    public Page<ArticleBriefViewResponse> findAllByPages(Pageable pageable) {
        Page<Article> postPage = articleRepository.findAll(pageable);

        return postPage.map(p -> converter.toArticleBriefViewResponse(p));
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
    public ArticleCreateOrUpdateResponse createOrUpdate(ArticleCreateOrUpdateRequest request, Long writerId) {
        Member writer = memberRepository.findById(writerId)
                .orElseThrow(() -> new BusinessException(ResponseMessage.MEMBER_NOT_FOUND));

        if(request.getImageUrls() == null || request.getImageUrls().isEmpty()){
            throw new BusinessException(ResponseMessage.EMPTY_IMAGE_URL);
        }

        Article articleEntity = null;

        if (request.getId() == null || request.getId().longValue() == 0) { // 신규 생성일 경우
            Article article = converter.toEntity(request, writer);
            articleEntity = articleRepository.save(article);
        } else {  // 수정일 경우
            articleEntity = articleRepository.findById(request.getId())
                    .orElseThrow(() -> new BusinessException(ResponseMessage.ARTICLE_NOT_FOUND));

//            if(!articleEntity.validateWriter(writerId)){    //TODO: 인증 부분 구현 후 주석 해제
//                throw new BusinessException(ResponseMessage.PERMISSION_DENIED);
//            }

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

        for (var imageUrl : request.getImageUrls()) {
            if(imageUrl == null || imageUrl.isEmpty()) continue;

            var productImage = new ProductImage(imageUrl, articleEntity);
            productImageRepository.save(productImage);
        }

        return converter.toArticleCreateOrUpdateResponse(articleEntity);
    }

    @Transactional
    public void updateTradeStatus(Long articleId, int code, Long writerId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BusinessException(ResponseMessage.ARTICLE_NOT_FOUND));

//        if(!article.validateWriter(writerId)){   // TODO: 인증 과정 구현 후 예정
//            throw new BusinessException(ResponseMessage.PERMISSION_DENIED);
//        }

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
    public void deleteOne(Long articleId, Long writerId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BusinessException(ResponseMessage.ARTICLE_NOT_FOUND));

//        if(!article.validateWriter(writerId)){   //TODO: 인증 부분 구현 후 주석 해제
//            throw new BusinessException(ResponseMessage.PERMISSION_DENIED);
//        }

        articleRepository.delete(article);
    }

    @Transactional(readOnly = true)
    public ArticleDetailResponse findById(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BusinessException(ResponseMessage.ARTICLE_NOT_FOUND));

        return converter.toArticleDetailResponse(article);
    }
}
