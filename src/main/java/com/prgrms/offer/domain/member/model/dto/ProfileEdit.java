package com.prgrms.offer.domain.member.model.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class ProfileEdit {
    @NotBlank
    private String nickname;
    @NotBlank
    private String address;
    @NotBlank
    private String profileImageUrl;

    @Override
    public String toString() {
        return "ProfileEdit{" +
                "nickname='" + nickname + '\'' +
                ", address='" + address + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                '}';
    }
}
