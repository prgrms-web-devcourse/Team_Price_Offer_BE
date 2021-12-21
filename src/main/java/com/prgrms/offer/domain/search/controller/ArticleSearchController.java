package com.prgrms.offer.domain.search.controller;

import com.prgrms.offer.common.ApiResponse;
import com.prgrms.offer.common.message.ResponseMessage;
import com.prgrms.offer.common.page.PageDto;
import com.prgrms.offer.common.page.PageInfo;
import com.prgrms.offer.core.jwt.JwtAuthentication;
import com.prgrms.offer.domain.article.model.dto.ArticleBriefViewResponse;
import com.prgrms.offer.domain.search.model.dto.SearchFilterRequest;
import com.prgrms.offer.domain.search.service.ArticleSearchService;
import java.util.Optional;
import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/search", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class ArticleSearchController {

    private final ArticleSearchService articleSearchService;

    @GetMapping()
    public ResponseEntity<ApiResponse> searchWithTitle(
        @RequestParam(value = "title") @NotBlank String title,
        @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC, size = 20) Pageable pageable,
        @AuthenticationPrincipal JwtAuthentication authentication) {
        Page<ArticleBriefViewResponse> articleBriefViewResponse = articleSearchService.findByTitle(
            title, pageable, Optional.ofNullable(authentication));

        PageInfo pageInfo = getPageInfo(articleBriefViewResponse);

        return ResponseEntity.ok(
            ApiResponse.of(ResponseMessage.SUCCESS,
                PageDto.of(articleBriefViewResponse.getContent(), pageInfo))
        );
    }

    @GetMapping(value = "/filters")
    public ResponseEntity<ApiResponse> searchByFilters(
        @RequestParam(value = "title") @Nullable String title,
        @RequestParam(value = "categoryCode") @Nullable Integer categoryCode,
        @RequestParam(value = "tradeMethodCode") @Nullable Integer tradeMethodCode,
        @RequestParam(value = "minPrice") @Nullable Integer minPrice,
        @RequestParam(value = "maxPrice") @Nullable Integer maxPrice,
        @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC, size = 20) Pageable pageable,
        @AuthenticationPrincipal JwtAuthentication authentication) {

        SearchFilterRequest searchFilterRequest = SearchFilterRequest.builder()
            .title(title)
            .categoryCode(categoryCode)
            .tradeMethodCode(tradeMethodCode)
            .minPrice(minPrice)
            .maxPrice(maxPrice).build();

        Page<ArticleBriefViewResponse> articleBriefViewResponsePage = articleSearchService.findByFilter(
            searchFilterRequest, pageable, Optional.ofNullable(authentication)
        );

        PageInfo pageInfo = getPageInfo(articleBriefViewResponsePage);

        return ResponseEntity.ok(
            ApiResponse.of(ResponseMessage.SUCCESS,
                PageDto.of(articleBriefViewResponsePage.getContent(), pageInfo)));

    }

    private PageInfo getPageInfo(Page<ArticleBriefViewResponse> pageResponses) {
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
