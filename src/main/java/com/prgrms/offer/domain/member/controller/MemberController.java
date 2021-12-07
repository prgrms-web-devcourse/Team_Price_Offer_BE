package com.prgrms.offer.domain.member.controller;

import com.prgrms.offer.common.ApiResponse;
import com.prgrms.offer.common.message.ResponseMessage;
import com.prgrms.offer.core.error.exception.BusinessException;
import com.prgrms.offer.core.jwt.JwtAuthentication;
import com.prgrms.offer.domain.member.model.dto.*;
import com.prgrms.offer.domain.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }


    @PostMapping("/members")
    public ResponseEntity<ApiResponse<MemberCreateResponse>> createUser(@RequestBody @Valid MemberCreateRequest request, BindingResult bindingResult) {
        MemberCreateResponse response = memberService.createMember(request);
        return ResponseEntity.ok(
                ApiResponse.of(ResponseMessage.SUCCESS, response)
        );
    }

    @PostMapping("/members/login")
    public ResponseEntity<ApiResponse<MemberResponse>> emailLogin(@RequestBody @Valid EmailLoginRequest request) {
        MemberResponse response = memberService.login(request);
        return ResponseEntity.ok(
                ApiResponse.of(ResponseMessage.SUCCESS, response)
        );
    }

    @GetMapping(path = "/user/me")
    public LoginResponse me(@AuthenticationPrincipal JwtAuthentication authentication) {
        return memberService.findByPrincipal(authentication.loginId)
                .map(member ->
                        new LoginResponse(authentication.token, authentication.loginId)
                )
                .orElseThrow(() -> new IllegalArgumentException("Could not found user for " + authentication.loginId));
    }


}
