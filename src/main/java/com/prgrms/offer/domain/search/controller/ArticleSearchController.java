package com.prgrms.offer.domain.search.controller;

import com.prgrms.offer.common.ApiResponse;
import com.prgrms.offer.common.message.ResponseMessage;
import com.prgrms.offer.core.jwt.JwtAuthentication;
import com.prgrms.offer.domain.article.model.dto.ArticleBriefViewResponse;
import com.prgrms.offer.domain.search.service.ArticleSearchService;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        Pageable pageable,
        @AuthenticationPrincipal JwtAuthentication authentication) {
        Page<ArticleBriefViewResponse> response = articleSearchService.findByTitle(title, pageable, authentication);

        return ResponseEntity.ok(
            ApiResponse.of(ResponseMessage.SUCCESS, response)
        );
    }

}
