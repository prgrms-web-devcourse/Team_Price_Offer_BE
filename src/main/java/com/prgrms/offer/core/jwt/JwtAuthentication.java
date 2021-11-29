package com.prgrms.offer.core.jwt;

import org.springframework.util.Assert;

public class JwtAuthentication {

    public final String token;
    public final String username;


    public JwtAuthentication(String token, String username) {
        Assert.hasText(token, "token must not be empty.");
        Assert.hasText(username, "username must not be empty.");
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
