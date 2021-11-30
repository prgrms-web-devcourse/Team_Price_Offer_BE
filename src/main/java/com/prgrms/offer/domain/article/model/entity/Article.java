package com.prgrms.offer.domain.article.model.entity;

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

    @Column(name = "like_count")
    private Integer likeCount = 0;

    @Column(name = "title")
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "category_code")
    private Integer categoryCode;

    @Column(name = "product_status_code")
    private Integer productStatusCode;

    @Column(name = "trade_area")
    private String tradeArea;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "trade_method_code")
    private Integer tradeMethodCode;

    @Column(name = "trade_status_code")
    private Integer tradeStatusCode;

    @Column(name = "main_image_url")
    private String mainImageUrl;

    @Column(name = "price")
    private Integer price;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "modified_date")
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
    }

    public void updateTradeMethodCode(int tradeMethodCode) {
        this.tradeMethodCode = tradeMethodCode;
    }

    public void addLikeCount(){
        this.likeCount++;
    }

    public void subtractLikeCount(){
        if(this.likeCount <= 0)
            throw new RuntimeException();

        this.likeCount--;
    }

    public void setMainImageUrl(String mainImageUrl) {
        this.mainImageUrl = mainImageUrl;
    }
}
