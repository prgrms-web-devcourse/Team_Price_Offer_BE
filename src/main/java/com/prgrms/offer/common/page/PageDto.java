package com.prgrms.offer.common.page;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PageDto<T> {

    private T elements;
    private PageInfo pageInfo;

    public static <T> PageDto<T> of(T elements, PageInfo pageInfo) {
        return new PageDto<>(elements, pageInfo);
    }

}
