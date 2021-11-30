package com.prgrms.offer.domain.article.model.value;

import com.prgrms.offer.common.message.ResponseMessage;
import com.prgrms.offer.core.error.exception.BusinessException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum TradeStatus {
    RESERVING("예약중", 2),
    COMPLETED("거래완료", 4),

    ;

    private final String name;
    private final int code;

    public static TradeStatus of(int code) {
        return Arrays.stream(TradeStatus.values())
                .filter(v -> v.code == code)
                .findFirst()
                .orElseThrow(() -> new BusinessException(ResponseMessage.TRADE_STATUS_NOT_FOUND));
    }

    public static TradeStatus of(String name) {
        return Arrays.stream(TradeStatus.values())
                .filter(v -> v.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ResponseMessage.TRADE_STATUS_NOT_FOUND));
    }
}
