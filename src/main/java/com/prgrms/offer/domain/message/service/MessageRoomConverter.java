package com.prgrms.offer.domain.message.service;

import com.prgrms.offer.domain.member.model.entity.Member;
import com.prgrms.offer.domain.message.model.dto.MessageRoomResponse;
import com.prgrms.offer.domain.message.model.dto.MessageRoomResponse.MessageInfo;
import com.prgrms.offer.domain.message.model.dto.MessageRoomResponse.UserInfo;
import com.prgrms.offer.domain.message.model.entity.Message;
import com.prgrms.offer.domain.message.model.entity.MessageRoom;
import org.springframework.stereotype.Component;

@Component
public class MessageRoomConverter {

    public MessageRoomResponse toMessageRoomResponse(MessageRoom messageRoom, Message message) {
        Member messagePartner = messageRoom.getMessagePartner();
        UserInfo userInfo = messagePartner !=null ? UserInfo.createUserInfo(messagePartner): UserInfo.nullUserInfo();

        return MessageRoomResponse.builder()
            .userInfo(userInfo)
            .productImageUrl(messageRoom.getArticle().getMainImageUrl())
            .message(new MessageInfo(message.getContent(), message.getCreatedDate()))
            .build();
    }

}
