package com.prgrms.offer.domain.article.controller;

import com.prgrms.offer.common.ApiResponse;
import com.prgrms.offer.common.message.ResponseMessage;
import com.prgrms.offer.core.error.exception.BusinessException;
import com.prgrms.offer.domain.article.model.dto.ArticleBriefViewResponse;
import com.prgrms.offer.domain.article.model.dto.CategoriesResponse;
import com.prgrms.offer.domain.article.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/articles", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "게시글 (Article)")
public class ArticleController {

    private final ArticleService articleService;

    @ApiOperation("이미지 -> URL 변환")
    @PostMapping(value = "/imageUrls", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> convertToImageUrls(@ModelAttribute List<MultipartFile> images) throws IOException {

        if(images == null || images.isEmpty()){
            throw new BusinessException(ResponseMessage.INVALID_IMAGE_EXCEPTION);
        }

        List<String> imageUrls = articleService.getImageUrls(images);

        Map response = new HashMap<String, List<String>>();
        response.put("imageUrls", imageUrls);

        return ResponseEntity.ok(
                ApiResponse.of(ResponseMessage.SUCCESS, response)
        );
    }

    @ApiOperation("게시글 전체 조회")
    @GetMapping()
    public ResponseEntity<ApiResponse> getAll(Pageable pageable) {
        Page<ArticleBriefViewResponse> response = articleService.findAllByPages(pageable);

        return ResponseEntity.ok(
                ApiResponse.of(ResponseMessage.SUCCESS, response)
        );
    }

    @ApiOperation("카테고리 목록 조회")
    @GetMapping(value = "/categories")
    public ResponseEntity<ApiResponse> getAllCategories() {
        CategoriesResponse response = articleService.findAllCategories();

        return ResponseEntity.ok(
                ApiResponse.of(ResponseMessage.SUCCESS, response)
        );
    }
}
