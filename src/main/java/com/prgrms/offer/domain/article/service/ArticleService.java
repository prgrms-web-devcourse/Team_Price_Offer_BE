package com.prgrms.offer.domain.article.service;

import com.prgrms.offer.common.message.ResponseMessage;
import com.prgrms.offer.common.utils.S3ImageUploader;
import com.prgrms.offer.core.error.exception.BusinessException;
import com.prgrms.offer.domain.article.model.dto.ArticleBriefViewResponse;
import com.prgrms.offer.domain.article.model.dto.ArticleCreateOrUpdateRequest;
import com.prgrms.offer.domain.article.model.dto.ArticleCreateOrUpdateResponse;
import com.prgrms.offer.domain.article.model.dto.CategoriesResponse;
import com.prgrms.offer.domain.article.model.entity.Article;
import com.prgrms.offer.domain.article.model.entity.ProductImage;
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

        for(var image : images) {
            String uploadedImageUrl = s3ImageUploader.upload(image, PRODUCT_IMAGE_DIR);
            imageUrls.add(uploadedImageUrl);
        }

        return imageUrls;
    }

    @Transactional
    public ArticleCreateOrUpdateResponse createOrUpdate(ArticleCreateOrUpdateRequest request, Long writerId) {
        Member writer = memberRepository.findById(writerId)
                .orElseThrow(() -> new BusinessException(ResponseMessage.MEMBER_NOT_FOUND));

        Article articleEntity = null;

        if(request.getId() == null || request.getId().longValue() == 0) { // 신규 생성일 경우
            Article article = converter.toEntity(request, writer);
            articleEntity = articleRepository.save(article);
        }
        else{  // 수정일 경우
            articleEntity = articleRepository.findById(request.getId())
                    .orElseThrow(() -> new BusinessException(ResponseMessage.ARTICLE_NOT_FOUND));

            productImageRepository.deleteAllByArticle(articleEntity);
        }

        if(request.getImageUrls() != null && !request.getImageUrls().isEmpty()){
            for(var imageUrl : request.getImageUrls()) {
                var productImage = new ProductImage(imageUrl, articleEntity);
                productImageRepository.save(productImage);
            }
        }

        return converter.toArticleCreateOrUpdateResponse(articleEntity);
    }
}
