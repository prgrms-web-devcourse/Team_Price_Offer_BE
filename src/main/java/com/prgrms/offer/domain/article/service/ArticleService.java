package com.prgrms.offer.domain.article.service;

import com.prgrms.offer.common.utils.S3ImageUploader;
import com.prgrms.offer.domain.article.model.dto.ArticleBriefViewResponse;
import com.prgrms.offer.domain.article.model.dto.CategoriesResponse;
import com.prgrms.offer.domain.article.model.entity.Article;
import com.prgrms.offer.domain.article.repository.ArticleRepository;
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
}
