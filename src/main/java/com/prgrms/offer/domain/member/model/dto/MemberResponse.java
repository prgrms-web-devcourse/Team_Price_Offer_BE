package com.prgrms.offer.domain.member.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponse {

    private final MemberDto member;

    @Getter
    public static class MemberDto {
        private final Long id;
        private final String token;
        private final String email;
        private final int offerLevel;
        private final String nickname;
        private final String profileImage;
        private final String address;

        @Builder
        public MemberDto(Long id, String token, String email, int offerLevel, String nickname, String profileImage, String address) {
            this.id = id;
            this.token = token;
            this.email = email;
            this.offerLevel = offerLevel;
            this.nickname = nickname;
            this.profileImage = profileImage;
            this.address = address;
        }

        @Override
        public String toString() {
            return "MemberDto{" +
                    "id=" + id +
                    ", token='" + token + '\'' +
                    ", email='" + email + '\'' +
                    ", appleLevel=" + offerLevel +
                    ", nickname='" + nickname + '\'' +
                    ", profileImage='" + profileImage + '\'' +
                    ", address='" + address + '\'' +
                    '}';
        }
    }

    public MemberResponse(MemberDto member) {
        this.member = member;
    }

    @Override
    public String toString() {
        return "MemberResponse{" +
                "member=" + member +
                '}';
    }
}
