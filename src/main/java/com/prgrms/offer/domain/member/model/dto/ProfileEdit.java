package com.prgrms.offer.domain.member.model.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class ProfileEdit {
    @NotBlank
    @Size(max = 20)
    private String nickname;

    @NotBlank
    @Size(max = 30)
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
