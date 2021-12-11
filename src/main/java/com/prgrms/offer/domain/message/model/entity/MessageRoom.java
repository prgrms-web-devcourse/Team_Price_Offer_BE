package com.prgrms.offer.domain.message.model.entity;

import com.prgrms.offer.domain.article.model.entity.Article;
import com.prgrms.offer.domain.member.model.entity.Member;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity
public class MessageRoom {

    @Id
    @GeneratedValue
    private Long messageRoomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "messagePartnerId")
    private Member messagePartner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articleId")
    private Article article;

    private LocalDateTime createdDate;

    public MessageRoom(Member member, Member messagePartner, Article article) {
        this.member = member;
        this.messagePartner = messagePartner;
        this.article = article;
        createdDate = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
    }

}
