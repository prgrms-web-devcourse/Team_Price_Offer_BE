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
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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


    public Page<MessageContentResponse> toMessageContentResponsePage(
        List<Message> messageList, Pageable pageable) {
        List<MessageContentResponse> messageContentResponseList =
            messageList.stream().map(
                    e -> new MessageContentResponse(e.getContent(), e.isSendMessage(), e.getCreatedDate()))
                .collect(Collectors.toList());

        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()),
            messageContentResponseList.size());
        final Page<MessageContentResponse> page = new PageImpl<>(
            messageContentResponseList.subList(start, end), pageable,
            messageContentResponseList.size());

        return page;
    }

    public MessageRoomInfoResponse toMessageRoomInfoResponse(Member messagePartner, Article article,
        Offer offer) {
        return new MessageRoomInfoResponse(
            MessageRoomInfoResponse.ArticleInfo.createArticleInfo(article, offer),
            MessageRoomInfoResponse.MessagePartnerInfo.createMessagePartnerInfo(messagePartner)
        );
    }

}
