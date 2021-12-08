package com.prgrms.offer.domain.member.model.dto;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static com.prgrms.offer.common.message.DtoValidationMessage.*;

@Getter
public class MemberCreateRequest {

    @NotBlank
    @Size(max = 20)
    private String nickname;

    @NotBlank
    @Size(max = 30)
    private String address;

    @Email
    private String email;

    @NotBlank
    @Size(min = 8, max = 15, message = INVALID_PASSWORD_LENGTH)
    private String password;

    @NotBlank
    private String confirmedPassword;

    @Override
    @Size(min = 8, max = 15, message = INVALID_PASSWORD_LENGTH)
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
