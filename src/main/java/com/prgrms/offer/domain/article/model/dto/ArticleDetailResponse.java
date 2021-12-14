package com.prgrms.offer.domain.article.model.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDetailResponse {
    private ArticleDto article;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ArticleDto{

        private Long id;

        private AuthorDetail author;

        private String title;

        private String content;

        private CodeAndName category;

        private CodeAndName tradeStatus;

        private CodeAndName productStatus;

        private String tradeArea;

        private CodeAndName tradeMethod;

        private int quantity;

        private int price;

        private String mainImageUrl;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdDate;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime modifiedDate;

        private int likeCount;

        private Boolean isLiked;

        private int viewCount;

    }

    @Builder
    @AllArgsConstructor
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public static class AuthorDetail{
        private Long id;

        private String email;

        private int appleLevel;

        private String nickname;

        private String profileImageUrl;

        private String address;
    }

}
