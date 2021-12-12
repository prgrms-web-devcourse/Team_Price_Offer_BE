package com.prgrms.offer.domain.article.controller;

import com.prgrms.offer.common.ApiResponse;
import com.prgrms.offer.common.message.ResponseMessage;
import com.prgrms.offer.common.page.PageDto;
import com.prgrms.offer.common.page.PageInfo;
import com.prgrms.offer.core.error.exception.BusinessException;
import com.prgrms.offer.core.jwt.JwtAuthentication;
import com.prgrms.offer.domain.article.model.dto.ArticleBriefViewResponse;
import com.prgrms.offer.domain.article.model.dto.LikeArticleStatusResponse;
import com.prgrms.offer.domain.article.service.LikeArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/articles", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "게시글 좋아요(찜하기)")
public class LikeArticleController {

    private final LikeArticleService likeArticleService;

    @ApiOperation("게시글 좋아요 상태 변경")
    @PutMapping(value = "/{articleId}/like")
    public ResponseEntity<ApiResponse> switchLikeArticleStatus(
            @PathVariable Long articleId,
            @AuthenticationPrincipal JwtAuthentication authentication
    ){

        validateJwtAuthentication(authentication);

        LikeArticleStatusResponse response = likeArticleService.switchLikeStatus(articleId, authentication);

        return ResponseEntity.ok(
                ApiResponse.of(ResponseMessage.SUCCESS, response)
        );
    }

    @GetMapping("/like-articles")
    public ResponseEntity<ApiResponse> getLikeArticles(
            @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC, size = 20) Pageable pageable,
            @AuthenticationPrincipal JwtAuthentication authentication) {
        Page<ArticleBriefViewResponse> responses = likeArticleService.getLikeArticles(pageable, authentication);
        PageInfo pageInfo = getPageInfo(responses);
        return ResponseEntity.ok(
                ApiResponse.of(ResponseMessage.SUCCESS, PageDto.of(responses.getContent(), pageInfo))
        );
    }

    private void validateJwtAuthentication(JwtAuthentication authentication){ // TODO: JwtAuthentication 로 관련 로직 이동
        if(authentication == null){
            throw new BusinessException(ResponseMessage.PERMISSION_DENIED);
        }
    }

    private PageInfo getPageInfo(Page<?> pageResponses) {
        return PageInfo.of(
                pageResponses.getPageable().getPageNumber(),
                pageResponses.getTotalPages(),
                pageResponses.getPageable().getPageSize(),
                pageResponses.getTotalElements(),
                pageResponses.isLast(),
                pageResponses.isFirst()
        );
    }
}
