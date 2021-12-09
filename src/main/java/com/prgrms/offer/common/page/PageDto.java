package com.prgrms.offer.common.page;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PageDto<T> {

    private T contents;
    private PageInfo pageInfo;

    public static <T> PageDto<T> of(T contents, PageInfo pageInfo) {
        return new PageDto<>(contents, pageInfo);
    }

}
