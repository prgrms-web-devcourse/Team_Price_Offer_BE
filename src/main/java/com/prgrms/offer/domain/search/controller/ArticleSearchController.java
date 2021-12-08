package com.prgrms.offer.domain.search.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.prgrms.offer.common.ApiResponse;
import com.prgrms.offer.common.message.ResponseMessage;
import com.prgrms.offer.domain.article.model.dto.ArticleBriefViewResponse;
import com.prgrms.offer.domain.article.model.entity.Article;
import com.prgrms.offer.domain.article.repository.ArticleRepository;
import com.prgrms.offer.domain.member.model.entity.Member;
import com.prgrms.offer.domain.member.model.entity.MemberRepository;
import com.prgrms.offer.domain.search.service.ArticleSearchService;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import javax.annotation.PostConstruct;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
        @RequestParam(value = "title") @NotBlank String title, Pageable pageable) {
        Page<ArticleBriefViewResponse> response = articleSearchService.findByTitle(title, pageable);

        return ResponseEntity.ok(
            ApiResponse.of(ResponseMessage.SUCCESS, response)
        );
    }

}
