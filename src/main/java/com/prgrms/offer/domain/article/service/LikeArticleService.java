package com.prgrms.offer.domain.article.service;

import com.prgrms.offer.domain.article.repository.LikeArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeArticleService {

    private final LikeArticleRepository likeArticleRepository;


}
