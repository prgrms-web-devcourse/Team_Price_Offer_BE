package com.prgrms.offer.core.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.offer.common.ApiResponse;
import com.prgrms.offer.common.message.ResponseMessage;
import com.prgrms.offer.core.jwt.Jwt;
import com.prgrms.offer.domain.member.model.dto.MemberResponse;
import com.prgrms.offer.domain.member.model.entity.Member;
import com.prgrms.offer.domain.member.service.MemberConverter;
import com.prgrms.offer.domain.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
* 인증이 완료되었을 때 핸들러
* */
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final Jwt jwt;
    private final MemberService memberService;
    private final MemberConverter memberConverter;

    public OAuth2AuthenticationSuccessHandler(Jwt jwt, MemberService memberService, MemberConverter memberConverter) {
        this.jwt = jwt;
        this.memberService = memberService;
        this.memberConverter = memberConverter;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
        OAuth2User principal = oauth2Token.getPrincipal();
        String registrationId = oauth2Token.getAuthorizedClientRegistrationId();

        Member member = processUserOAuth2UserJoin(principal, registrationId);
        String token = generateToken(member);

        String targetUrl = UriComponentsBuilder.fromUriString("https://offerprice.vercel.app/oauth/kakao")
                .queryParam("token", token)
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);

    }

    private Member processUserOAuth2UserJoin(OAuth2User oAuth2User, String registrationId) {
        return memberService.join(oAuth2User, registrationId);
    }

    private String generateToken(Member member) {
        return jwt.sign(Jwt.Claims.from(member.getPrincipal(), new String[]{}));
    }
}
