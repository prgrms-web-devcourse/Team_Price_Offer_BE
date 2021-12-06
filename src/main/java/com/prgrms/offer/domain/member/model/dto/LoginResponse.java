package com.prgrms.offer.domain.member.model.dto;

import lombok.Getter;

@Getter
public class LoginResponse {
    private final String token;
    private final String loginId;

    public LoginResponse(String token, String loginId) {
        this.token = token;
        this.loginId = loginId;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "token='" + token + '\'' +
                ", loginId='" + loginId + '\'' +
                '}';
    }
}
