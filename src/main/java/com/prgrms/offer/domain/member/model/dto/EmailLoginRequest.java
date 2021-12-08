package com.prgrms.offer.domain.member.model.dto;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class EmailLoginRequest {
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, max = 15)
    private String password;

    @Override
    public String toString() {
        return "EmailLoginRequest{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
