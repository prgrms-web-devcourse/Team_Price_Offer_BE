package com.prgrms.offer.domain.message.controller;

import com.prgrms.offer.common.ApiResponse;
import com.prgrms.offer.common.message.ResponseMessage;
import com.prgrms.offer.common.page.PageDto;
import com.prgrms.offer.common.page.PageInfo;
import com.prgrms.offer.core.error.exception.BusinessException;
import com.prgrms.offer.core.jwt.JwtAuthentication;
import com.prgrms.offer.domain.member.service.MemberService;
import com.prgrms.offer.domain.message.model.dto.MessageRequest;
import com.prgrms.offer.domain.message.model.dto.MessageRoomResponse;
import com.prgrms.offer.domain.message.service.MessageService;
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

    @PostMapping("/member/{memberId}")
    public ResponseEntity<ApiResponse> sendFirstMessageToOfferer(
        @PathVariable Long memberId,
        @RequestParam(value = "articleId") long articleId,
        @RequestBody MessageRequest messageRequest,
        @AuthenticationPrincipal JwtAuthentication authentication) {

        validateJwtAuthentication(authentication);

        messageService.sendFirstMessageToOfferer(memberId, authentication.loginId, articleId,
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
