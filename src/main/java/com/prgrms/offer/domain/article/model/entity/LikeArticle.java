package com.prgrms.offer.domain.article.model.entity;

import com.prgrms.offer.domain.member.model.entity.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(indexes = {
        @Index(name = "like_article_idx_member_id_article_id", columnList = "member_id, article_id", unique = true),
        @Index(name = "like_article_idx_member_id", columnList = "member_id")
})
public class LikeArticle {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "like_article_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", referencedColumnName = "article_id")
    private Article article;

    public LikeArticle(Member member, Article article) {
        this.member = member;
        this.article = article;
    }
}
