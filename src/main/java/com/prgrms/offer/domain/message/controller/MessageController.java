package com.prgrms.offer.domain.message.controller;

import com.prgrms.offer.common.ApiResponse;
import com.prgrms.offer.common.message.ResponseMessage;
import com.prgrms.offer.common.page.PageDto;
import com.prgrms.offer.common.page.PageInfo;
import com.prgrms.offer.core.error.exception.BusinessException;
import com.prgrms.offer.core.jwt.JwtAuthentication;
import com.prgrms.offer.domain.member.service.MemberService;
import com.prgrms.offer.domain.message.model.dto.MessageContentResponse;
import com.prgrms.offer.domain.message.model.dto.MessageRequest;
import com.prgrms.offer.domain.message.model.dto.MessageRoomInfoResponse;
import com.prgrms.offer.domain.message.model.dto.MessageRoomResponse;
import com.prgrms.offer.domain.message.model.dto.OutgoingMessageResponse;
import com.prgrms.offer.domain.message.service.MessageService;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/messages")
@RestController
public class MessageController {

    private final MemberService memberService;
    private final MessageService messageService;

    @PostMapping("/member/{memberId}/offerId/{offerId}")
    public ResponseEntity<ApiResponse> sendMessageToOffererOnclickMessageButton(
        @PathVariable Long memberId,
        @RequestParam(value = "articleId") long articleId,
        @PathVariable @Min(1) long offerId,
        @RequestBody MessageRequest messageRequest,
        @AuthenticationPrincipal JwtAuthentication authentication) {

        validateJwtAuthentication(authentication);

        messageService.sendMessageToOffererOnclickMessageButton(memberId, authentication.loginId,
            articleId,
            offerId,
            messageRequest.getContent());

        return ResponseEntity.ok(ApiResponse.of(ResponseMessage.SUCCESS));
    }

    @GetMapping("/messageBox")
    public ResponseEntity<ApiResponse> getMessageBox(Pageable pageable,
        @AuthenticationPrincipal JwtAuthentication authentication) {

        validateJwtAuthentication(authentication);

        Page<MessageRoomResponse> messageRoomResponsePage = messageService.getMessageBox(
            authentication.loginId, pageable);

        PageInfo pageInfo = getPageInfo(messageRoomResponsePage);

        return ResponseEntity.ok(
            ApiResponse.of(ResponseMessage.SUCCESS,
                PageDto.of(messageRoomResponsePage.getContent(), pageInfo))
        );
    }

    // request param, pathvariable notnull, body not null 체크하기
    @PostMapping("/messageRoom/{messageRoomId}")
    public ResponseEntity<ApiResponse> sendMessage(
        @PathVariable @Min(1) long messageRoomId,
        @RequestBody @NotNull MessageRequest messageRequest,
        @AuthenticationPrincipal JwtAuthentication authentication) {

        validateJwtAuthentication(authentication);

        OutgoingMessageResponse messageResponse = messageService.sendMessage(messageRoomId,
            messageRequest, authentication.loginId
        );

        return ResponseEntity.ok(ApiResponse.of(ResponseMessage.SUCCESS, messageResponse));
    }

    // 대화방 단건 조회 : 상대방과의 쪽지 내용 조회
    @GetMapping("/messageRoom/{messageRoomId}/contents")
    public ResponseEntity<ApiResponse> getMessageRoomContents(
        @PathVariable @Min(1) long messageRoomId,
        Pageable pageable,
        @AuthenticationPrincipal JwtAuthentication authentication) {

        validateJwtAuthentication(authentication);

        Page<MessageContentResponse> messageContentResponsePage =
            messageService.getMessageRoomContents(messageRoomId, authentication.loginId, pageable);

        PageInfo pageInfo = getPageInfo(messageContentResponsePage);

        return ResponseEntity.ok(
            ApiResponse.of(
                ResponseMessage.SUCCESS,
                PageDto.of(
                    messageContentResponsePage.getContent(), pageInfo
                )
            )
        );

    }

    @GetMapping("/messageRoom/{messageRoomId}/messageRoomInfo")
    public ResponseEntity<ApiResponse> getMessageRoom(
        @PathVariable @Min(1) long messageRoomId,
        @AuthenticationPrincipal JwtAuthentication authentication) {

        validateJwtAuthentication(authentication);

        MessageRoomInfoResponse messageRoomInfoResponse
            = messageService.getMessageRoomInfo(messageRoomId, authentication.loginId);


        return ResponseEntity.ok(ApiResponse.of(ResponseMessage.SUCCESS, messageRoomInfoResponse));
    }

    private void validateJwtAuthentication(JwtAuthentication authentication) {
        if (authentication == null) {
            throw new BusinessException(ResponseMessage.PERMISSION_DENIED);
        }
    }

    private PageInfo getPageInfo(Page<?> pageResponses) {
        return PageInfo.of(
            pageResponses.getPageable().getPageNumber(),
            pageResponses.getTotalPages(),
            pageResponses.getPageable().getPageSize(),
            pageResponses.getTotalElements(),
            pageResponses.isLast(),
            pageResponses.isFirst()
        );
    }

}
