package com.prgrms.offer.common.page;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageDto<T> {

    private T elements;
    private PageInfo pageInfo;

    private Integer offerCountOfCurrentMember;

    public static <T> PageDto<T> of(T elements, PageInfo pageInfo) {
        return new PageDto<>(elements, pageInfo, null);
    }

    public static <T> PageDto<T> of(T elements, PageInfo pageInfo, int offerCountOfCurrentMember) {
        return new PageDto<>(elements, pageInfo, offerCountOfCurrentMember);
    }

}
