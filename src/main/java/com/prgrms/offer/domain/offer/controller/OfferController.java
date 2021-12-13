package com.prgrms.offer.domain.offer.controller;

import com.prgrms.offer.common.ApiResponse;
import com.prgrms.offer.common.message.ResponseMessage;
import com.prgrms.offer.common.page.PageDto;
import com.prgrms.offer.common.page.PageInfo;
import com.prgrms.offer.core.error.exception.BusinessException;
import com.prgrms.offer.core.jwt.JwtAuthentication;
import com.prgrms.offer.domain.offer.model.dto.OfferCreateRequest;
import com.prgrms.offer.domain.offer.model.dto.OfferResponse;
import com.prgrms.offer.domain.offer.service.OfferService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "가격 제안 관련")
public class OfferController {

    private final OfferService offerService;

    @ApiOperation("가격 제안하기")
    @PostMapping(value = "/articles/{articleId}/offers", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> offer(
            @PathVariable Long articleId,
            @Valid @RequestBody OfferCreateRequest request,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {

        validateJwtAuthentication(authentication);

        OfferResponse response = offerService.offer(request, articleId, authentication);

        return ResponseEntity.ok(
                ApiResponse.of(ResponseMessage.SUCCESS, response)
        );
    }

    @ApiOperation("단건 게시글 offer리스트 조회(페이징)")
    @GetMapping(value = "/articles/{articleId}/offers")
    public ResponseEntity<ApiResponse> getAllByArticleId(
            @PathVariable Long articleId,
            @PageableDefault(sort = "price", direction = Sort.Direction.DESC, size = 20) Pageable pageable,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {

        Page<OfferResponse> pageResponses = offerService.findAllByArticleId(pageable, articleId);

        int offerCountOfCurrentMember = offerService.findOfferCountOfCurrentMember(authentication, articleId);

        PageInfo pageInfo = getPageInfo(pageResponses);

        return ResponseEntity.ok(
                ApiResponse.of(ResponseMessage.SUCCESS, PageDto.of(pageResponses.getContent(), pageInfo, offerCountOfCurrentMember))
        );
    }

    @ApiOperation("가격채택")
    @PatchMapping(value = "/articles/offers/{offerId}")
    public ResponseEntity<ApiResponse> adopteOffer(@PathVariable Long offerId, @AuthenticationPrincipal JwtAuthentication authentication) {
        validateJwtAuthentication(authentication);

        offerService.adopteOffer(offerId, authentication);

        return ResponseEntity.ok(
                ApiResponse.of(ResponseMessage.SUCCESS)
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
