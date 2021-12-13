package com.prgrms.offer.domain.message.model.entity;

import com.prgrms.offer.domain.article.model.entity.Article;
import com.prgrms.offer.domain.member.model.entity.Member;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import javax.persistence.*;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageRoomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_partner_id")
    private Member messagePartner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    private LocalDateTime createdDate;

    public MessageRoom(Member member, Member messagePartner, Article article) {
        this.member = member;
        this.messagePartner = messagePartner;
        this.article = article;
        createdDate = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
    }

}
