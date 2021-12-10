package com.prgrms.offer.common.page;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PageInfo {

    private int currentPageNumber;
    private int lastPageNumber;

    private int sizePerPage;
    private int totalElementCount;

    private Boolean isLastPage;
    private Boolean isFirstPage;

    public static PageInfo of(
            int currentPageNumber,
            int lastPageNumber,
            int sizePerPage,
            long totalElementCount,
            boolean isLastPage,
            boolean isFirstPage
    ) {

        return new PageInfo(
                currentPageNumber + 1,
                lastPageNumber,
                sizePerPage,
                (int) totalElementCount,
                isLastPage,
                isFirstPage
        );
    }

}
