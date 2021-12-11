package com.prgrms.offer.domain.message.service;

import com.prgrms.offer.domain.member.model.entity.Member;
import com.prgrms.offer.domain.message.model.entity.Message;
import com.prgrms.offer.domain.message.model.entity.MessageRoom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Component;

@Component
public class MessageConverter {

    public Message createMessage(Member sender, Member receiver, String content,
        MessageRoom messageRoom) {
        return Message.builder()
            .sender(sender)
            .receiver(receiver)
            .content(content)
            .createdDate(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS))
            .messageRoom(messageRoom)
            .build();
    }

}
