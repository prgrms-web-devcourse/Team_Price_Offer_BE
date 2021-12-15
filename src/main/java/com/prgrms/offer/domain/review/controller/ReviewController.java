package com.prgrms.offer.domain.review.controller;

import com.prgrms.offer.common.ApiResponse;
import com.prgrms.offer.common.message.ResponseMessage;
import com.prgrms.offer.common.page.PageDto;
import com.prgrms.offer.common.page.PageInfo;
import com.prgrms.offer.core.error.exception.BusinessException;
import com.prgrms.offer.core.jwt.JwtAuthentication;
import com.prgrms.offer.domain.review.model.dto.ReviewCreateRequest;
import com.prgrms.offer.domain.review.model.dto.ReviewCreateResponse;
import com.prgrms.offer.domain.review.model.dto.ReviewResponse;
import com.prgrms.offer.domain.review.service.ReviewService;
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

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "리뷰")
public class ReviewController {

    private final ReviewService reviewService;

    @ApiOperation("리뷰 남기기 통합")
    @PostMapping(value = "/reviews")
    public ResponseEntity<ApiResponse> createReview(
            @RequestParam(value = "articleId", required = true) Long articleId,
            @Valid @RequestBody ReviewCreateRequest request,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {

        validateJwtAuthentication(authentication);

        ReviewCreateResponse response = reviewService.createReview(articleId, request, authentication);

        return ResponseEntity.ok(
                ApiResponse.of(ResponseMessage.SUCCESS, response)
        );
    }

    /*
    @ApiOperation("판매자가 구매자에게 거래후기 남기기")  //deprecated
    @PostMapping(value = "/reviews/offers/{offerId}")
    public ResponseEntity<ApiResponse> createReviewToBuyer(
            @PathVariable(required = true) Long offerId,
            @RequestParam(value = "revieweeId", required = true) Long revieweeId,
            @Valid @RequestBody ReviewCreateRequest request,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {

        validateJwtAuthentication(authentication);

        ReviewCreateResponse response = reviewService.createReviewToBuyer(offerId, revieweeId, request, authentication);

        return ResponseEntity.ok(
                ApiResponse.of(ResponseMessage.SUCCESS, response)
        );
    }

    @ApiOperation("구매자가 판매자에게 거래후기 남기기")  //deprecated
    @PostMapping(value = "/reviews")
    public ResponseEntity<ApiResponse> createReviewToSeller(
            @RequestParam(value = "articleId", required = true) Long articleId,
            @RequestParam(value = "revieweeId", required = true) Long revieweeId,
            @Valid @RequestBody ReviewCreateRequest request,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {

        validateJwtAuthentication(authentication);

        ReviewCreateResponse response = reviewService.createReviewToSeller(articleId, revieweeId, request, authentication);

        return ResponseEntity.ok(
                ApiResponse.of(ResponseMessage.SUCCESS, response)
        );
    }

     */

    @ApiOperation("특정 사용자가 받은 리뷰 모두 조회(구매자로서 또는 판매자로서)")
    @GetMapping(value = "/reviews")
    public ResponseEntity<ApiResponse> getAll(
            @RequestParam(value = "memberId", required = true) Long memberId,
            @RequestParam(value = "role", required = true) String role,
            @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC, size = 20) Pageable pageable,
            @AuthenticationPrincipal JwtAuthentication authentication
    ){

        Page<ReviewResponse> pageResponses = reviewService.findAllByRole(pageable, memberId, role, Optional.ofNullable(authentication));

        PageInfo pageInfo = getPageInfo(pageResponses);

        return ResponseEntity.ok(
                ApiResponse.of(ResponseMessage.SUCCESS, PageDto.of(pageResponses.getContent(), pageInfo))
        );
    }

    private void validateJwtAuthentication(JwtAuthentication authentication) { // TODO: JwtAuthentication 로 관련 로직 이동
        if (authentication == null) {
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
