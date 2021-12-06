package com.prgrms.offer.domain.member.model.entity;

import com.prgrms.offer.common.ApiResponse;
import com.prgrms.offer.common.message.ResponseMessage;
import com.prgrms.offer.core.jwt.JwtAuthentication;
import com.prgrms.offer.core.jwt.JwtAuthenticationToken;
import com.prgrms.offer.domain.member.model.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;

    private final AuthenticationManager authenticationManager;

    public MemberController(MemberService memberService, AuthenticationManager authenticationManager) {
        this.memberService = memberService;
        this.authenticationManager = authenticationManager;
    }


    @PostMapping("/members")
    public ResponseEntity<ApiResponse<MemberCreateResponse>> createUser(@RequestBody MemberCreateRequest request) {
        Member member = memberService.createMember(request);
        MemberCreateResponse response = toMemberCreateResponse(member);
        return ResponseEntity.ok(
                ApiResponse.of(ResponseMessage.SUCCESS, response)
        );
    }

    @PostMapping("/members/login")
    public ResponseEntity<ApiResponse<MemberResponse>> emailLogin(@RequestBody EmailLoginRequest request) {
        JwtAuthenticationToken token = new JwtAuthenticationToken(request.getEmail(), request.getPassword());
        Authentication resultToken = authenticationManager.authenticate(token);
        JwtAuthentication authentication = (JwtAuthentication) resultToken.getPrincipal();
        Member member = (Member) resultToken.getDetails();

        MemberResponse response = toMemberResponse(member, authentication.token);
        return ResponseEntity.ok(
                ApiResponse.of(ResponseMessage.SUCCESS, response)
        );
    }

    private MemberCreateResponse toMemberCreateResponse(Member member) {
        MemberCreateResponse.MemberDto memberDto = MemberCreateResponse.MemberDto.builder()
                .id(member.getId())
                .address(member.getAddress())
                .email(member.getPrincipal())
                .nickname(member.getNickname())
                .build();
        return new MemberCreateResponse(memberDto);
    }

    private MemberResponse toMemberResponse(Member member, String token) {
        MemberResponse.MemberDto memberDto = MemberResponse.MemberDto.builder()
                .id(member.getId())
                .email(member.getPrincipal())
                .token(token)
                .nickname(member.getNickname())
                .appleLevel(member.getAppleLevel())
                .address(member.getAddress())
                .profileImage(member.getProfileImage())
                .build();
        return new MemberResponse(memberDto);
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
