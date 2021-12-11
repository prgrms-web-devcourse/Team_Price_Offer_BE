package com.prgrms.offer.domain.message.repository;

import com.prgrms.offer.domain.article.model.entity.Article;
import com.prgrms.offer.domain.member.model.entity.Member;
import com.prgrms.offer.domain.message.model.entity.MessageRoom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

public interface MessageRoomRepository extends Repository<MessageRoom, Long> {

    List<MessageRoom> findByMemberIdOrderByCreatedDateDesc(Long userId, Pageable pageable);

    MessageRoom save(MessageRoom messageRoom);

    Optional<MessageRoom> findByMemberAndMessagePartnerAndArticle(Member me, Member partner,
        Article artcile);

}
