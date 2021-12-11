package com.prgrms.offer.domain.message.service;

import com.prgrms.offer.common.message.ResponseMessage;
import com.prgrms.offer.core.error.exception.BusinessException;
import com.prgrms.offer.domain.article.model.entity.Article;
import com.prgrms.offer.domain.article.repository.ArticleRepository;
import com.prgrms.offer.domain.member.model.entity.Member;
import com.prgrms.offer.domain.member.repository.MemberRepository;
import com.prgrms.offer.domain.message.model.dto.MessageRoomResponse;
import com.prgrms.offer.domain.message.model.entity.Message;
import com.prgrms.offer.domain.message.model.entity.MessageRoom;
import com.prgrms.offer.domain.message.repository.MessageRepository;
import com.prgrms.offer.domain.message.repository.MessageRoomRepository;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageRoomRepository messageRoomRepository;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;
    private final MessageRoomConverter messageRoomConverter;

    // 이미 보낸적 있는 사람에겐 채팅방 생성하지 않고 기존 채팅방 사용하기
    @Transactional
    public void sendFirstMessageToOfferer(long receiverId, String senderLoginId, long articleId,
        String content) {

        Member receiver = memberRepository.findById(receiverId)
            .orElseThrow(() -> new BusinessException(ResponseMessage.MEMBER_NOT_FOUND));

        Member sender = memberRepository.findByPrincipal(senderLoginId)
            .orElseThrow(() -> new BusinessException(ResponseMessage.MEMBER_NOT_FOUND));

        Article article = articleRepository.findById(articleId)
            .orElseThrow(() -> new BusinessException(ResponseMessage.ARTICLE_NOT_FOUND));

        Boolean isMyMessageRoomExists = true;
        Boolean isOffererMessageRoomExits = true;

        MessageRoom myMessageRoom = messageRoomRepository.findByMemberAndMessagePartnerAndArticle(
            sender, receiver, article).orElseGet(
            () -> createMessageRoom(sender, receiver, article, isMyMessageRoomExists)
        );


        MessageRoom offererMessageRoom = messageRoomRepository.findByMemberAndMessagePartnerAndArticle(
            receiver, sender, article).orElseGet(
            () -> createMessageRoom(receiver, sender, article, isOffererMessageRoomExits)
        );


        Message myMessage = Message.builder()
            .sender(sender)
            .receiver(receiver)
            .content(content)
            .createdDate(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS))
            .messageRoom(myMessageRoom)
            .build();

        Message offererMessage = Message.builder()
            .sender(sender)
            .receiver(receiver)
            .content(content)
            .createdDate(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS))
            .messageRoom(offererMessageRoom)
            .build();

        messageRepository.save(myMessage);
        messageRepository.save(offererMessage);

    }

    @Transactional
    public Page<MessageRoomResponse> getMessageBox(String loginId, Pageable pageable) {
        Member me = memberRepository.findByPrincipal(loginId).get();

        List<MessageRoom> messageRoomList = messageRoomRepository.findByMemberIdOrderByCreatedDateDesc(
            me.getId(), pageable);

        List<Message> messageList = messageRoomList.stream().map(
            messageRoom -> messageRepository.findTop1ByMessageRoomOrderByCreatedDateDesc(
                messageRoom)).collect(Collectors.toList());

        List<MessageRoomResponse> messageRoomResponseList = new ArrayList<>();

        Iterator<MessageRoom> messageRoomIterator = messageRoomList.listIterator();
        Iterator<Message> messageIterator = messageList.listIterator();

        while (messageRoomIterator.hasNext()) {
            messageRoomResponseList.add(
                messageRoomConverter.toMessageRoomResponse(messageRoomIterator.next(),
                    messageIterator.next()));
        }

        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), messageRoomResponseList.size());
        final Page<MessageRoomResponse> page = new PageImpl<>(messageRoomResponseList.subList(start, end), pageable, messageRoomResponseList.size());

        return page;

    }

    public MessageRoom createMessageRoom(Member member1, Member member2, Article article, Boolean isMessageRoomExists) {
        if (!isMessageRoomExists) isMessageRoomExists = !isMessageRoomExists;
        return messageRoomRepository.save(new MessageRoom(member1, member2, article));
    }

}
