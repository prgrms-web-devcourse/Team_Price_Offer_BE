package com.prgrms.offer.domain.article.model.entity;

import com.prgrms.offer.common.message.ResponseMessage;
import com.prgrms.offer.core.error.exception.BusinessException;
import com.prgrms.offer.domain.member.model.entity.Member;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "article_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", referencedColumnName = "member_id")
    private Member writer;

    @Column
    private Integer likeCount = 0;

    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column
    private Integer categoryCode;

    @Column
    private Integer productStatusCode;

    @Column
    private String tradeArea;

    @Column
    private Integer quantity;

    @Column
    private Integer tradeMethodCode;

    @Column
    private Integer tradeStatusCode;

    @Column
    private String mainImageUrl;

    @Column
    private Integer price;

    @Column
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column
    private LocalDateTime modifiedDate;

    public void updateInfo(String title, String content, int categoryCode, String tradeArea, int quantity) {
        this.title = title;
        this.content = content;
        this.categoryCode = categoryCode;
        this.tradeArea = tradeArea;
        this.quantity = quantity;
        modifiedDate = LocalDateTime.now();
    }

    public void updateStatusCode(int tradeStatusCode) {
        this.tradeStatusCode = tradeStatusCode;
        modifiedDate = LocalDateTime.now();
    }

    public void updateTradeMethodCode(int tradeMethodCode) {
        this.tradeMethodCode = tradeMethodCode;
        modifiedDate = LocalDateTime.now();
    }

    public void addLikeCount(){
        this.likeCount++;
    }

    public void subtractLikeCount(){
        if(this.likeCount <= 0)
            throw new BusinessException(ResponseMessage.INTERNAL_SERVER_ERROR);

        this.likeCount--;
    }

    public void updateMainImageUrl(String mainImageUrl) {
        this.mainImageUrl = mainImageUrl;
        modifiedDate = LocalDateTime.now();
    }
}
