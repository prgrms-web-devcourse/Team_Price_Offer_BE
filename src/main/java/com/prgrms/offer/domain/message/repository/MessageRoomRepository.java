package com.prgrms.offer.domain.message.repository;

import com.prgrms.offer.domain.article.model.entity.Article;
import com.prgrms.offer.domain.member.model.entity.Member;
import com.prgrms.offer.domain.message.model.entity.MessageRoom;
import com.prgrms.offer.domain.offer.model.entity.Offer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface MessageRoomRepository extends Repository<MessageRoom, Long> {

    List<MessageRoom> findByMemberId(Long userId, Pageable pageable);

    MessageRoom save(MessageRoom messageRoom);

    Optional<MessageRoom> findByMemberAndMessagePartnerAndArticle(Member me, Member partner,
        Article artcile);

    Optional<MessageRoom> findById(long messageRoomId);

    Optional<MessageRoom> findByMemberAndOffer(Member messagePartner, Offer offer);

    Long countMessageRoomByMember(Member me);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE MessageRoom mr SET mr.article = NULL WHERE mr.article = :article")
    void doOnDeleteSetNullFromArticle(Article article);
}
