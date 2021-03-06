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

    // ?????? ????????? ?????? ???????????? ????????? ???????????? ?????? ?????? ????????? ????????????
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

        // ???????????? ????????? ?????????(?????? ?????? ??????)
        // messageRoom ???????????? ??????????????? ???????????????

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

    // ????????? ????????????
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

    // ??????????????? ????????? ??????
    @Transactional
    public OutgoingMessageResponse sendMessage(
        long messageRoomId,
        MessageRequest messageRequest,
        String loginId) {
        // ?????? ???????????? ?????? ??????
        MessageRoom myMessageRoom = messageRoomRepository.findById(messageRoomId)
            .orElseThrow(() -> new BusinessException(ResponseMessage.EXITED_MESSAGE_ROOM));

        isAuthenticatedUser(loginId, myMessageRoom);

        // ???????????? ???????????? ?????? ??????
        Member messagePartner = myMessageRoom.getMessagePartner();

        if (messagePartner == null) {
            throw new BusinessException(ResponseMessage.MEMBER_NOT_FOUND);
        }

        MessageRoom receiverMessageRoom = messageRoomRepository.findByMemberAndOffer(
                messagePartner, myMessageRoom.getOffer())
            .orElseThrow(
                () -> new BusinessException(ResponseMessage.MESSAGE_PARTNER_EXITED_MESSAGE_ROOM));

//        receiverMessageRoom?????? receiver ?????? vs myMessageRoom?????? messagePartner ??????
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

        // ?????? ????????? ???????????? ????????? ??????
        if (me.getId() != myMessageRoom.getMember().getId()) {
            throw new BusinessException(ResponseMessage.PERMISSION_DENIED);
        }

        return me;
    }

}
