package com.prgrms.offer.domain.member.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class LoginResponse {

    private final String token;
    private final String username;

    @Override
    public String toString() {
        return "LoginResponse{" +
            "token='" + token + '\'' +
            ", username='" + username + '\'' +
            '}';
    }
}
