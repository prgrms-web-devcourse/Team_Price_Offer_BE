package com.prgrms.offer.domain.article.service;

import com.prgrms.offer.domain.article.model.dto.LikeArticleStatusResponse;
import org.springframework.stereotype.Component;

@Component
public class LikeArticleConverter {
    public LikeArticleStatusResponse toLikeArticleStatusResponse(boolean isLiked){
        return new LikeArticleStatusResponse(isLiked);
    }
}
