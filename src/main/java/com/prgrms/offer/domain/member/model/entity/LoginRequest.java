package com.prgrms.offer.domain.member.model.entity;

public class LoginRequest {
    private String principal;
    private String credentials;

    protected LoginRequest() {};

    public LoginRequest(String principal, String credentials) {
        this.principal = principal;
        this.credentials = credentials;
    }

    public String getPrincipal() {
        return principal;
    }

    public String getCredentials() {
        return credentials;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "principal='" + principal + '\'' +
                ", credentials='" + credentials + '\'' +
                '}';
    }
}
