package com.prgrms.offer.core.jwt;

import org.springframework.util.Assert;

/**
* 인증 완료 후 인증된 사용자를 표현하기 위한 객체
* JwtAuthenticationToken의 principal에 해당
* */
public class JwtAuthentication {

    public final String token;
    public final String loginId;

    public JwtAuthentication(String token, String loginId) {
        Assert.hasText(token, "token must not be empty");
        Assert.hasText(loginId, "loginId must not be empty");
        this.token = token;
        this.loginId = loginId;
    }

    @Override
    public String toString() {
        return "JwtAuthentication{" +
                "token='" + token + '\'' +
                ", loginId='" + loginId + '\'' +
                '}';
    }
}
