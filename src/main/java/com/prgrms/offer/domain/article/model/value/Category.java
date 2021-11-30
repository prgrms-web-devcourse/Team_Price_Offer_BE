package com.prgrms.offer.domain.article.model.value;

import com.prgrms.offer.common.message.ResponseMessage;
import com.prgrms.offer.core.error.exception.BusinessException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum Category {
    HOT("인기매물", 1),
    DIGITAL_DEVICES("디지털기기", 2),
    HOME_APPLIANCES("생활가전", 3),
    FURNITURE_INTERIOR("가구/인테리어", 4),
    CHILDREN("유아동", 5),
    DAILY_LIFE_PROCESSED_FOOD("생활/가공식품", 6),
    CHILDREN_BOOKS("유아도서", 7),
    SPORTS_LEISURE("스포츠/레저", 8),
    GENERAL_MERCHANDISE_FOR_WOMEN("여성잡화", 9),
    WOMEN_CLOTHING("여성의류", 10),
    MEN_FASHION("남성패션/잡화", 11),
    GAMES_HOBBIES("게임/취미", 12),
    BEAUTY("뷰티/미용", 13),
    PET_PRODUCTS("반려동물용품", 14),
    BOOKS_TICKETS_RECORDS("도서/티켓/음반", 15),
    PLANTS("식물", 16),
    FOOD("식품", 17),
    OTHER_USED_ITEMS("기타 중고물품", 18),
    BUY("삽니다", 19),

    ;

    private final String name;
    private final int code;

    public static Category of(int code) {
        return Arrays.stream(Category.values())
                .filter(v -> v.code == code)
                .findFirst()
                .orElseThrow(() -> new BusinessException(ResponseMessage.CATEGORY_NOT_FOUND));
    }

    public static Category of(String name) {
        return Arrays.stream(Category.values())
                .filter(v -> v.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ResponseMessage.CATEGORY_NOT_FOUND));
    }

    public static List<String> getAllCategoryName() {
        final List<String> result = new ArrayList<>();

        Arrays.stream(Category.values())
                .map(v -> result.add(v.getName()));

        return result;
    }
}
