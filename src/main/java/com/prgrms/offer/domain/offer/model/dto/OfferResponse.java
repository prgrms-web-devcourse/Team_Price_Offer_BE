package com.prgrms.offer.domain.offer.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OfferResponse {
    private final OfferDto offer;

    private final Integer offerCountOfCurrentMember;

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class OfferDto{
        private final Long id;

        private final OffererDto offerer;

        private final Long articleId;

        private final int price;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private final LocalDateTime createdDate;

        private final Boolean isSelected;
    }

    @Getter
    @RequiredArgsConstructor
    public static class OffererDto{
        private final Long id;

        private final String nickname;

        private final String address;

        private final int offerLevel;
    }
}
