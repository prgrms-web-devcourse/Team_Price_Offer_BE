package com.prgrms.offer.domain.member.model.entity;

import com.prgrms.offer.core.jwt.JwtAuthentication;
import com.prgrms.offer.core.jwt.JwtAuthenticationToken;
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


    @GetMapping(path = "/user/me")
    public LoginResponse me(@AuthenticationPrincipal JwtAuthentication authentication) {
        return memberService.findByLoginId(authentication.loginId)
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
