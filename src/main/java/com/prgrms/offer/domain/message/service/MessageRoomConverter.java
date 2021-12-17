package com.prgrms.offer.domain.message.service;

import com.prgrms.offer.domain.member.model.entity.Member;
import com.prgrms.offer.domain.message.model.dto.MessageRoomResponse;
import com.prgrms.offer.domain.message.model.dto.MessageRoomResponse.MessageInfo;
import com.prgrms.offer.domain.message.model.dto.MessageRoomResponse.UserInfo;
import com.prgrms.offer.domain.message.model.entity.Message;
import com.prgrms.offer.domain.message.model.entity.MessageRoom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class MessageRoomConverter {

    public MessageRoomResponse toMessageRoomResponse(MessageRoom messageRoom, Message message) {
        Member messagePartner = messageRoom.getMessagePartner();
        UserInfo userInfo = messagePartner != null ? UserInfo.createUserInfo(messagePartner)
            : UserInfo.createNullUserInfo();

        return MessageRoomResponse.builder()
            .userInfo(userInfo)
            .productImageUrl(messageRoom.getArticle().getMainImageUrl())
            .message(new MessageInfo(message.getContent(), message.getCreatedDate()))
            .messageRoomId(messageRoom.getMessageRoomId())
            .build();
    }

    public Page<MessageRoomResponse> toMessageRoomResponsePage(
        List<MessageRoom> messageRoomList,
        List<Message> messageList,
        long numMessageRoom,
        Pageable pageable) {

        List<MessageRoomResponse> messageRoomResponseList = new ArrayList<>();

        Iterator<MessageRoom> messageRoomIterator = messageRoomList.listIterator();
        Iterator<Message> messageIterator = messageList.listIterator();

        while (messageRoomIterator.hasNext()) {
            messageRoomResponseList.add(
                toMessageRoomResponse(messageRoomIterator.next(), messageIterator.next())
            );
        }

        final Page<MessageRoomResponse> page = new PageImpl<>(
            messageRoomResponseList, pageable, numMessageRoom);

        return page;
    }

}
