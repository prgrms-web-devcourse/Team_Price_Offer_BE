package com.prgrms.offer.common.page;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class CollectionToPage {

    public static Page<?> toPage(List<?> list, Pageable pageable, long numContent) {
        final Page<?> page =
            new PageImpl<>(list, pageable, numContent);

        return page;
    }
}
