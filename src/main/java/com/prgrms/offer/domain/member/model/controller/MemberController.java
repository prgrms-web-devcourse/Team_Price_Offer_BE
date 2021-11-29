package com.prgrms.offer.domain.member.model.controller;

import com.prgrms.offer.common.ApiResponse;
import com.prgrms.offer.core.jwt.JwtAuthentication;
import com.prgrms.offer.core.jwt.JwtAuthenticationToken;
import com.prgrms.offer.domain.member.model.dto.LoginRequest;
import com.prgrms.offer.domain.member.model.dto.LoginResponse;
import com.prgrms.offer.domain.member.model.entity.Member;
import com.prgrms.offer.domain.member.model.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/emailLogin")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        JwtAuthenticationToken authToken = new JwtAuthenticationToken(
            request.getPrincipal(), request.getCredential());
        Authentication resultToken = authenticationManager.authenticate(authToken);
        JwtAuthenticationToken authenticated = (JwtAuthenticationToken) resultToken;
        JwtAuthentication principal = (JwtAuthentication) authenticated.getPrincipal();
        Member member = (Member) authenticated.getDetails();
        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK,
            "로그인 성공",
            new LoginResponse(principal.token, principal.username))
        );
    }

}
