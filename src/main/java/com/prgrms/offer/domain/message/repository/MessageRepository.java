package com.prgrms.offer.domain.message.repository;

import com.prgrms.offer.domain.message.model.entity.Message;
import com.prgrms.offer.domain.message.model.entity.MessageRoom;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

public interface MessageRepository extends Repository<Message, Long> {

    Message save(Message message);

    Message findTop1ByMessageRoomOrderByCreatedDateDesc(MessageRoom messageRoom);

    List<Message> findByMessageRoom(MessageRoom messageRoom, Pageable pageable);
}
