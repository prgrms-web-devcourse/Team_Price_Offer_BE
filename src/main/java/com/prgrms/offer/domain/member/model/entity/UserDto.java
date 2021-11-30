package com.prgrms.offer.domain.member.model.entity;

public class UserDto {
    private final String token;
    private final String username;
    private final String group;

    public UserDto(String token, String username, String group) {
        this.token = token;
        this.username = username;
        this.group = group;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getGroup() {
        return group;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "token='" + token + '\'' +
                ", username='" + username + '\'' +
                ", group='" + group + '\'' +
                '}';
    }
}
