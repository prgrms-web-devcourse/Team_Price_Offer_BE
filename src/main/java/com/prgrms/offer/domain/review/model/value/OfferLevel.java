package com.prgrms.offer.domain.review.model.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OfferLevel {
    LEVEL1(1),
    LEVEL2(2),
    LEVEL3(3),
    LEVEL4(4),
    LEVEL5(5),
    LEVEL6(6),

    ;

    private final int level;

    public static OfferLevel calculateOfferLevel(int score){
        if(score <= 3) return LEVEL1;
        if(score <= 7) return LEVEL2;
        if(score <= 15) return LEVEL3;
        if(score <= 31) return LEVEL4;
        if(score <= 63) return LEVEL5;

        return LEVEL6;
    }
}
