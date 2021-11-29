package com.prgrms.offer.domain.member.model.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LoginRequest {

    private String principal;
    private String credential;

    public LoginRequest(String principal, String credential) {
        this.principal = principal;
        this.credential = credential;
    }
}

