package com.prgrms.offer.domain.article.model.entity;

import com.prgrms.offer.common.message.ResponseMessage;
import com.prgrms.offer.core.error.exception.BusinessException;
import com.prgrms.offer.domain.article.model.value.TradeStatus;
import com.prgrms.offer.domain.member.model.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(indexes = {
        @Index(name = "article_idx_writer_id", columnList = "writer_id"),
        @Index(name = "article_idx_category_code", columnList = "categoryCode"),
        @Index(name = "article_idx_trade_status_code", columnList = "tradeStatusCode"),
        @Index(name = "article_idx_writer_id_trade_status_code", columnList = "writer_id, tradeStatusCode")
})
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(columnDefinition = "TEXT")
    private String mainImageUrl;

    @Column
    private Integer price;

    @Column
    private Integer viewCount;

    @Column
    private LocalDateTime createdDate;

    @Column
    private LocalDateTime modifiedDate;

    public void updateInfo(String title, String content, int categoryCode, String tradeArea, int quantity, int price) {
        this.title = title;
        this.content = content;
        this.categoryCode = categoryCode;
        this.tradeArea = tradeArea;
        this.quantity = quantity;
        this.price = price;
        modifiedDate = LocalDateTime.now();
    }

    public void updateTradeStatusCode(int tradeStatusCode) {
        if(TradeStatus.isCompleted(this.tradeStatusCode)){
            throw new BusinessException(ResponseMessage.ALREADY_SWITCH_TRADESTATUS);
        }

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

    public void addViewCount(){
        this.viewCount++;
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

    public boolean validateWriterByPrincipal(String principal){
        return this.writer.getPrincipal().equals(principal) ? true : false;
    }

    public boolean validateWriterByWriterId(Long writerId){
        return this.writer.getId().longValue() == writerId.longValue();
    }

}
