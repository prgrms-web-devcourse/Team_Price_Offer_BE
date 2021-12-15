package com.prgrms.offer.domain.member.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberProfile {

    private final MemberDto member;
    private final long sellingArticleCount;
    private final long reviewCount;

    public MemberProfile(MemberDto member, long sellingArticleCount, long reviewCount) {
        this.member = member;
        this.sellingArticleCount = sellingArticleCount;
        this.reviewCount = reviewCount;
    }

    @Getter
    public static class MemberDto {

        private final Long id;
        private final int offerLevel;
        private final String nickname;
        private final String profileImageUrl;
        private final String address;

        @Builder
        public MemberDto(Long id, int offerLevel, String nickname, String profileImageUrl, String address) {
            this.id = id;
            this.offerLevel = offerLevel;
            this.nickname = nickname;
            this.profileImageUrl = profileImageUrl;
            this.address = address;
        }

        @Override
        public String toString() {
            return "MemberDto{" +
                    "id=" + id +
                    ", offerLevel=" + offerLevel +
                    ", nickname='" + nickname + '\'' +
                    ", profileImageUrl='" + profileImageUrl + '\'' +
                    ", address='" + address + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "MemberProfile{" +
                "member=" + member +
                ", sellingArticleCount=" + sellingArticleCount +
                ", reviewCount=" + reviewCount +
                '}';
    }
}
