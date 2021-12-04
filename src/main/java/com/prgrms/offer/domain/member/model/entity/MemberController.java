package com.prgrms.offer.domain.member.model.entity;

import com.prgrms.offer.common.ApiResponse;
import com.prgrms.offer.common.message.ResponseMessage;
import com.prgrms.offer.core.jwt.JwtAuthentication;
import com.prgrms.offer.core.jwt.JwtAuthenticationToken;
import com.prgrms.offer.domain.member.model.dto.LoginRequest;
import com.prgrms.offer.domain.member.model.dto.LoginResponse;
import com.prgrms.offer.domain.member.model.dto.MemberCreateRequest;
import com.prgrms.offer.domain.member.model.dto.MemberCreateResponse;
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
        MemberCreateResponse response = getMemberResponse(member);
        return ResponseEntity.ok(
                ApiResponse.of(ResponseMessage.SUCCESS, response)
        );
    }

    private MemberCreateResponse getMemberResponse(Member member) {
        MemberCreateResponse.MemberDto memberDto = MemberCreateResponse.MemberDto.builder()
                .id(member.getId())
                .address(member.getAddress())
                .email(member.getPrincipal())
                .nickname(member.getNickname())
                .build();
        return MemberCreateResponse.builder().member(memberDto).build();
    }

    @GetMapping(path = "/user/me")
    public LoginResponse me(@AuthenticationPrincipal JwtAuthentication authentication) {
        return memberService.findByPrincipal(authentication.loginId)
                .map(member ->
                        new LoginResponse(authentication.token, authentication.loginId)
                )
                .orElseThrow(() -> new IllegalArgumentException("Could not found user for " + authentication.loginId));
    }


    @PostMapping(path = "/user/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        JwtAuthenticationToken authToken = new JwtAuthenticationToken(request.getPrincipal(), request.getCredentials());
        Authentication resultToken = authenticationManager.authenticate(authToken);
        JwtAuthentication authentication = (JwtAuthentication) resultToken.getPrincipal();
        Member member = (Member) resultToken.getDetails();
        return new LoginResponse(authentication.token, authentication.loginId);
    }

}
