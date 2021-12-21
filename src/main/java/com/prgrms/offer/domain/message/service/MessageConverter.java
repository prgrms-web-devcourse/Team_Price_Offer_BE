package com.prgrms.offer.domain.message.service;

import com.prgrms.offer.domain.article.model.entity.Article;
import com.prgrms.offer.domain.member.model.entity.Member;
import com.prgrms.offer.domain.message.model.dto.MessageContentResponse;
import com.prgrms.offer.domain.message.model.dto.MessageRoomInfoResponse;
import com.prgrms.offer.domain.message.model.dto.OutgoingMessageResponse;
import com.prgrms.offer.domain.message.model.entity.Message;
import com.prgrms.offer.domain.message.model.entity.MessageRoom;
import com.prgrms.offer.domain.offer.model.entity.Offer;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Component;

@Component
public class MessageConverter {

    public Message createMessage(boolean isSendMessage, String content, MessageRoom messageRoom) {
        return Message.builder()
            .isSendMessage(isSendMessage)
            .content(content)
            .createdDate(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS))
            .messageRoom(messageRoom)
            .build();
    }

    public OutgoingMessageResponse toOutgoingMessageResponse(String content,
        LocalDateTime localDateTime) {
        return new OutgoingMessageResponse(content, localDateTime);
    }


    public MessageContentResponse toMessageContentResponsePage(
        Message message) {

        MessageContentResponse messageContentResponse = new MessageContentResponse(
            message.getMessageId(),
            message.getContent(),
            message.isSendMessage(),
            message.getCreatedDate()
        );

        return messageContentResponse;
    }

    public MessageRoomInfoResponse toMessageRoomInfoResponse(Member messagePartner, Article article,
        Offer offer, long lastPageOfMessageContents) {
        return new MessageRoomInfoResponse(
            MessageRoomInfoResponse.ArticleInfo.createArticleInfo(article, offer),
            MessageRoomInfoResponse.MessagePartnerInfo.createMessagePartnerInfo(messagePartner),
            lastPageOfMessageContents
        );
    }


}
