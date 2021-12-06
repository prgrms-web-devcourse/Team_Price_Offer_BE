package com.prgrms.offer.core.oauth2;

import com.prgrms.offer.core.jwt.Jwt;
import com.prgrms.offer.domain.member.model.entity.Member;
import com.prgrms.offer.domain.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

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

    public OAuth2AuthenticationSuccessHandler(Jwt jwt, MemberService memberService) {
        this.jwt = jwt;
        this.memberService = memberService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        // TODO: 2021/12/06 멤버 응답 반환 로직 작성
    }

    private Member processUserOAuth2UserJoin(OAuth2User oAuth2User, String registrationId) {
        return memberService.join(oAuth2User, registrationId);
    }

    private String generateToken(Member member) {
        return jwt.sign(Jwt.Claims.from(member.getPrincipal(), new String[]{}));
    }
}
