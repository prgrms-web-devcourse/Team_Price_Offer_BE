package com.prgrms.offer.core.jwt;

import org.springframework.util.Assert;

/*
* 인증 완료 후 인증된 사용자를 표현하기 위한 객체
* JwtAuthenticationToken의 principal에 해당
* */
public class JwtAuthentication {

    public final String token;
    public final String username;

    public JwtAuthentication(String token, String username) {
        Assert.hasText(token, "token must not be empty");
        Assert.hasText(username, "username must not be empty");
        this.token = token;
        this.username = username;
    }

    @Override
    public String toString() {
        return "JwtAuthentication{" +
                "token='" + token + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
