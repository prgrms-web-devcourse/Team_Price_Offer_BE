package com.prgrms.offer.domain.offer.model.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@RequiredArgsConstructor
public class OfferBriefResponse {
    private final Long id;

    private final OfferResponse.OffererDto offerer;

    private final Long articleId;

    private final int price;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime createdDate;

    private final Boolean isSelected;

    @RequiredArgsConstructor
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public static class OffererDto{
        private final Long id;

        private final String nickname;

        private final String address;
    }
}
