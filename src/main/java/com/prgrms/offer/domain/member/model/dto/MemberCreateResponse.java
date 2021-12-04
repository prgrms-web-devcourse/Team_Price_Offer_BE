package com.prgrms.offer.domain.member.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberCreateResponse {

    private final MemberDto member;

    @Getter
    public static class MemberDto {
        private final Long id;
        private final String email;
        private final String nickname;
        private final String address;

        @Builder
        public MemberDto(Long id, String email, String nickname, String address) {
            this.id = id;
            this.email = email;
            this.nickname = nickname;
            this.address = address;
        }

        @Override
        public String toString() {
            return "MemberDto{" +
                    "id=" + id +
                    ", email='" + email + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", address='" + address + '\'' +
                    '}';
        }
    }

    @Builder
    public MemberCreateResponse(MemberDto member) {
        this.member = member;
    }

    @Override
    public String toString() {
        return "MemberResponse{" +
                "member=" + member +
                '}';
    }
}