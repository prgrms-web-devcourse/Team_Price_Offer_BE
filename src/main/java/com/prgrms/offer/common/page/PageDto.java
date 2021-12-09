package com.prgrms.offer.common.page;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PageDto<T> {

    private T data;
    private PageInfo pageInfo;

    public static <T> PageDto<T> of(T data, PageInfo pageInfo) {
        return new PageDto<>(data, pageInfo);
    }

}
