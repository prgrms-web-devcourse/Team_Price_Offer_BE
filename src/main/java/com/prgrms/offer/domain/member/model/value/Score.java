package com.prgrms.offer.domain.member.model.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Score {

    GOOD(2),
    NOT_BAD(1),
    BAD(-1),
    ;

    private final int value;
}
