package com.prgrms.offer.domain.article.controller;

import com.prgrms.offer.common.ApiResponse;
import com.prgrms.offer.common.message.ResponseMessage;
import com.prgrms.offer.core.error.exception.BusinessException;
import com.prgrms.offer.core.jwt.JwtAuthentication;
import com.prgrms.offer.domain.article.model.dto.*;
import com.prgrms.offer.domain.article.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @ApiOperation("게시글 등록/수정")
    @PutMapping()
    public ResponseEntity<ApiResponse> putArticle(
            @Valid @RequestBody ArticleCreateOrUpdateRequest request,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {

        validateJwtAuthentication(authentication);

        ArticleCreateOrUpdateResponse response = articleService.createOrUpdate(request, authentication);

        return ResponseEntity.ok(
                ApiResponse.of(ResponseMessage.SUCCESS, response)
        );
    }

    @ApiOperation("판매 상태 변경")
    @PatchMapping(value = "/{articleId}/tradeStatus", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> updateTradeStatus(
            @PathVariable Long articleId,
            @Valid @RequestBody TradeStatusUpdateRequest request,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {

        validateJwtAuthentication(authentication);

        articleService.updateTradeStatus(articleId, request.getCode(), authentication.loginId);

        return ResponseEntity.ok(
                ApiResponse.of(ResponseMessage.SUCCESS)
        );
    }

    @ApiOperation("게시글 전체 조회")
    @GetMapping()
    public ResponseEntity<ApiResponse> getAll(
            Pageable pageable,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {

        Page<ArticleBriefViewResponse> response = articleService.findAllByPages(pageable, Optional.ofNullable(authentication));

        return ResponseEntity.ok(
                ApiResponse.of(ResponseMessage.SUCCESS, response)
        );
    }

    @ApiOperation("게시글 단건 조회")
    @GetMapping(value = "/{articleId}")
    public ResponseEntity<ApiResponse> getOne(
            @PathVariable Long articleId,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {

        ArticleDetailResponse response = articleService.findById(articleId, Optional.ofNullable(authentication));

        return ResponseEntity.ok(
                ApiResponse.of(ResponseMessage.SUCCESS, response)
        );
    }

    @ApiOperation("단건 게시글의 이미지 전체 조회")
    @GetMapping(value = "/{articleId}/imageUrls")
    public ResponseEntity<ApiResponse> getAllImageUrls(@PathVariable Long articleId) {
        ProductImageUrlsResponse response = articleService.findAllImageUrls(articleId);

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

    @ApiOperation("게시글 삭제")
    @DeleteMapping(value = "/{articleId}")
    public ResponseEntity<ApiResponse> deleteOne(
            @PathVariable Long articleId,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {

        validateJwtAuthentication(authentication);

        articleService.deleteOne(articleId, authentication.loginId);

        return ResponseEntity.ok(
                ApiResponse.of(ResponseMessage.SUCCESS)
        );
    }

    private void validateJwtAuthentication(JwtAuthentication authentication){ // TODO: JwtAuthentication 로 관련 로직 이동
        if(authentication == null){
            throw new BusinessException(ResponseMessage.PERMISSION_DENIED);
        }
    }

}
