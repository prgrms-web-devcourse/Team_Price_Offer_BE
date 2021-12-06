package com.prgrms.offer.domain.member.model.dto;

import lombok.Getter;

@Getter
public class EmailLoginRequest {
    private String email;
    private String password;

    @Override
    public String toString() {
        return "EmailLoginRequest{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
