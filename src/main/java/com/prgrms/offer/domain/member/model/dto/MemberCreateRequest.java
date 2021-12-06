package com.prgrms.offer.domain.member.model.dto;

import lombok.Getter;

@Getter
public class MemberCreateRequest {

    private String nickname;
    private String address;
    private String email;
    private String password;
    private String confirmedPassword;

    @Override
    public String toString() {
        return "MemberCreateRequest{" +
                "nickname='" + nickname + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", confirmedPassword='" + confirmedPassword + '\'' +
                '}';
    }
}
