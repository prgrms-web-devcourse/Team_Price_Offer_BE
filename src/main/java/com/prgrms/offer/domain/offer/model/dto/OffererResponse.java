package com.prgrms.offer.domain.offer.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OffererResponse {
    private final Long id;

    private final String nickname;

    private final String address;

    private final int offerLevel;

    private final String profileImageUrl;
}
