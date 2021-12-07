package com.prgrms.offer.domain.article.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ArticleDetailResponse {
    private Long id;

    private AuthorDetail author;

    private String title;

    private String content;

    private CodeAndName category;

    private CodeAndName tradeStatus;

    private String tradeArea;

    private CodeAndName tradeMethod;

    private int quantity;

    private int price;

    private String mainImageUrl;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedDate;

    private int likeCounts;

    private boolean liked;

    private int viewCount;

    @Builder
    @AllArgsConstructor
    public static class AuthorDetail{
        private Long id;

        private String email;

        private int appleLevel;

        private String nickname;

        private String profileImageUrl;

        private String address;
    }

    @Getter
    @AllArgsConstructor
    public static class CodeAndName {
        private int code;

        private String name;
    }
}
