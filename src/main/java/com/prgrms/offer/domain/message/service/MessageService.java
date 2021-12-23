package com.prgrms.offer.domain.message.service;

import com.prgrms.offer.common.message.ResponseMessage;
import com.prgrms.offer.core.config.PropertyProvider;
import com.prgrms.offer.core.error.exception.BusinessException;
import com.prgrms.offer.domain.article.model.entity.Article;
import com.prgrms.offer.domain.article.repository.ArticleRepository;
import com.prgrms.offer.domain.member.model.entity.Member;
import com.prgrms.offer.domain.member.repository.MemberRepository;
import com.prgrms.offer.domain.message.model.dto.MessageContentResponse;
import com.prgrms.offer.domain.message.model.dto.MessageRequest;
import com.prgrms.offer.domain.message.model.dto.MessageRoomInfoResponse;
import com.prgrms.offer.domain.message.model.dto.MessageRoomResponse;
import com.prgrms.offer.domain.message.model.dto.OutgoingMessageResponse;
import com.prgrms.offer.domain.message.model.entity.Message;
import com.prgrms.offer.domain.message.model.entity.MessageRoom;
import com.prgrms.offer.domain.message.repository.MessageRepository;
import com.prgrms.offer.domain.message.repository.MessageRoomRepository;
import com.prgrms.offer.domain.offer.model.entity.Offer;
import com.prgrms.offer.domain.offer.repository.OfferRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageRoomRepository messageRoomRepository;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;
    private final MessageConverter messageConverter;
    private final MessageRoomConverter messageRoomConverter;
    private final OfferRepository offerRepository;

    private final PropertyProvider propertyProvider;

    // 이미 보낸적 있는 사람에겐 채팅방 생성하지 않고 기존 채팅방 사용하기
    @Transactional
    public void sendMessageToOffererOnclickMessageButton(long receiverId, String senderLoginId,
        long articleId,
        long offerId,
        String content) {

        Member receiver = memberRepository.findById(receiverId)
            .orElseThrow(() -> new BusinessException(ResponseMessage.MEMBER_NOT_FOUND));

        Member sender = memberRepository.findByPrincipal(senderLoginId)
            .orElseThrow(() -> new BusinessException(ResponseMessage.MEMBER_NOT_FOUND));

        Article article = articleRepository.findById(articleId)
            .orElseThrow(() -> new BusinessException(ResponseMessage.ARTICLE_NOT_FOUND));

        Offer offer = offerRepository.findById(offerId)
            .orElseThrow(() -> new BusinessException(ResponseMessage.OFFER_NOT_FOUND));

        Boolean isMyMessageRoomExists = true;
        Boolean isOffererMessageRoomExits = true;

        // 쪽지버튼 눌러서 보낼때(최초 전송 이후)
        // messageRoom 찾아오는 쿼리메소드 간소화하기

        MessageRoom myMessageRoom = messageRoomRepository.findByMemberAndMessagePartnerAndArticle(
            sender, receiver, article).orElseGet(
            () -> createMessageRoom(sender, receiver, article, isMyMessageRoomExists, offer)
        );

        MessageRoom offererMessageRoom = messageRoomRepository.findByMemberAndMessagePartnerAndArticle(
            receiver, sender, article).orElseGet(
            () -> createMessageRoom(receiver, sender, article, isOffererMessageRoomExits, offer)
        );

        Message myMessage = messageConverter.createMessage(true, content,
            myMessageRoom);
        Message offererMessage = messageConverter.createMessage(false, content,
            offererMessageRoom);

        messageRepository.save(myMessage);
        messageRepository.save(offererMessage);
    }

    // 쪽지함 가져오기
    @Transactional(readOnly = true)
    public Page<MessageRoomResponse> getMessageBox(String loginId, Pageable pageable) {
        Member me = memberRepository.findByPrincipal(loginId).get();

        List<MessageRoom> messageRoomList = messageRoomRepository.findByMemberId(me.getId(), pageable);

        List<Message> messageList = messageRoomList.stream().map(
            messageRoom -> messageRepository.findTop1ByMessageRoomOrderByCreatedDateDesc(
                messageRoom)).collect(Collectors.toList());

        long numMessageRoom = messageRoomRepository.countMessageRoomByMember(me);

        Page<MessageRoomResponse> messageRoomResponsePage =
            messageRoomConverter.toMessageRoomResponsePage(messageRoomList, messageList, numMessageRoom, pageable);

        return messageRoomResponsePage;
    }

    // 대화방에서 메시지 전송
    @Transactional
    public OutgoingMessageResponse sendMessage(
        long messageRoomId,
        MessageRequest messageRequest,
        String loginId) {
        // 내가 대화방을 나간 상황
        MessageRoom myMessageRoom = messageRoomRepository.findById(messageRoomId)
            .orElseThrow(() -> new BusinessException(ResponseMessage.EXITED_MESSAGE_ROOM));

        isAuthenticatedUser(loginId, myMessageRoom);

        // 상대방이 대화방을 나간 상황
        Member messagePartner = myMessageRoom.getMessagePartner();

        if (messagePartner == null) {
            throw new BusinessException(ResponseMessage.MEMBER_NOT_FOUND);
        }

        MessageRoom receiverMessageRoom = messageRoomRepository.findByMemberAndOffer(
                messagePartner, myMessageRoom.getOffer())
            .orElseThrow(
                () -> new BusinessException(ResponseMessage.MESSAGE_PARTNER_EXITED_MESSAGE_ROOM));

//        receiverMessageRoom으로 receiver 찾기 vs myMessageRoom에서 messagePartner 찾기
//        Member receiver = memberRepository.findById(receiverMessageRoom.getMember().getId())
//            .orElseThrow(() -> new BusinessException(ResponseMessage.MEMBER_NOT_FOUND));

        String content = messageRequest.getContent();

        Message myMessage = messageConverter.createMessage(true, content,
            myMessageRoom);
        Message receiverMessage = messageConverter.createMessage(false, content,
            receiverMessageRoom);

        messageRepository.save(myMessage);
        messageRepository.save(receiverMessage);

        return messageConverter.toOutgoingMessageResponse(myMessage.getContent(),
            myMessage.getCreatedDate());

    }

    @Transactional(readOnly = true)
    public Page<MessageContentResponse> getMessageRoomContents(long messageRoomId, String loginId,
        Pageable pageable) {

        MessageRoom myMessageRoom = messageRoomRepository.findById(messageRoomId)
            .orElseThrow(() -> new BusinessException(ResponseMessage.MESSAGE_ROOM_NOT_FOUND));

        isAuthenticatedUser(loginId, myMessageRoom);

        Page<Message> messageContentPage = messageRepository.findByMessageRoomOrderByMessageIdAsc(
            myMessageRoom, pageable);
        long numMessage = messageRepository.countAllByMessageRoom(myMessageRoom);

        return messageContentPage.map(message -> messageConverter.toMessageContentResponsePage(message));
    }

    @Transactional(readOnly = true)
    public MessageRoomInfoResponse getMessageRoomInfo(long messageRoomId, String loginId) {

        MessageRoom myMessageRoom = messageRoomRepository.findById(messageRoomId)
            .orElseThrow(() -> new BusinessException(ResponseMessage.MESSAGE_ROOM_NOT_FOUND));

        isAuthenticatedUser(loginId, myMessageRoom);

        Member messagePartner = myMessageRoom.getMessagePartner();

        Offer offer = myMessageRoom.getOffer();
        Article article = myMessageRoom.getArticle();

        long numMessageContent = messageRepository.countAllByMessageRoom(myMessageRoom);

        long lastPageOfMessageContents = (long) Math.ceil(
            numMessageContent / propertyProvider.getREQURIED_CONTENTS_SIZE());

        return messageConverter.toMessageRoomInfoResponse(messagePartner, article, offer,
            lastPageOfMessageContents);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public MessageRoom createMessageRoom(Member member1, Member member2, Article article,
        Boolean isMessageRoomExists, Offer offer) {
        if (!isMessageRoomExists) {
            isMessageRoomExists = !isMessageRoomExists;
        }
        return messageRoomRepository.save(new MessageRoom(member1, member2, article, offer));
    }

    private Member isAuthenticatedUser(String loginId, MessageRoom myMessageRoom) {
        Member me = memberRepository.findByPrincipal(loginId)
            .orElseThrow(() -> new BusinessException(ResponseMessage.MEMBER_NOT_FOUND));

        // 다른 멤버의 대화방에 접근한 경우
        if (me.getId() != myMessageRoom.getMember().getId()) {
            throw new BusinessException(ResponseMessage.PERMISSION_DENIED);
        }

        return me;
    }

}
