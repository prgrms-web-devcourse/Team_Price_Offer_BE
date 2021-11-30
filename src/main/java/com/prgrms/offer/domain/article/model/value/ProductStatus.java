package com.prgrms.offer.domain.article.model.value;

import com.prgrms.offer.common.message.ResponseMessage;
import com.prgrms.offer.core.error.exception.BusinessException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ProductStatus {
    NEW("새상품", 2),
    OLD("중고상품", 4),

    ;

    private final String name;
    private final int code;

    public static ProductStatus of(int code) {
        return Arrays.stream(ProductStatus.values())
                .filter(v -> v.code == code)
                .findFirst()
                .orElseThrow(() -> new BusinessException(ResponseMessage.PRODUCT_STATUS_NOT_FOUND));
    }

    public static ProductStatus of(String name) {
        return Arrays.stream(ProductStatus.values())
                .filter(v -> v.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ResponseMessage.PRODUCT_STATUS_NOT_FOUND));
    }
}
