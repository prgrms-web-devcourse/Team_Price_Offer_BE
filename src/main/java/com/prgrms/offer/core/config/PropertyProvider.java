package com.prgrms.offer.core.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Getter
@PropertySource("service.properties")
public class PropertyProvider {
    // for article
    private final int NUM_OF_REGISTERABLE_IMG;
    private final String NO_IMG;
    private final String PRODUCT_IMG_DIR;

    // for member
    private final String PROFILE_IMG_DIR;

    // for offer
    private final int MAX_AVAIL_OFFER_COUNT;

    // for review
    private final String BUYER;
    private final String SELLER;

    // for message
    private final double REQURIED_CONTENTS_SIZE;

    public PropertyProvider(
            @Value("${article.num_of_registerable_img}") int NUM_OF_REGISTERABLE_IMG,
            @Value("${article.no_img}") String NO_IMG,
            @Value("${article.product_img_dir}") String PRODUCT_IMG_DIR,
            @Value("${member.profile_img_dir}") String PROFILE_IMG_DIR,
            @Value("${offer.max_avail_offer_count}") int MAX_AVAIL_OFFER_COUNT,
            @Value("${review.buyer}") String BUYER,
            @Value("${review.seller}") String SELLER,
            @Value("${message.requried_contents_size}") double REQURIED_CONTENTS_SIZE
    ) {

        this.NUM_OF_REGISTERABLE_IMG = NUM_OF_REGISTERABLE_IMG;
        this.NO_IMG = NO_IMG;
        this.PRODUCT_IMG_DIR = PRODUCT_IMG_DIR;
        this.PROFILE_IMG_DIR = PROFILE_IMG_DIR;
        this.MAX_AVAIL_OFFER_COUNT = MAX_AVAIL_OFFER_COUNT;
        this.BUYER = BUYER;
        this.SELLER = SELLER;
        this.REQURIED_CONTENTS_SIZE = REQURIED_CONTENTS_SIZE;
    }
}
