package com.prgrms.offer.domain.member.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MyProfile {

    private final MemberDto member;
    private final long sellingArticleCount;
    private final long likedArticleCount;
    private final long offerCount;
    private final long reviewCount;

    public MyProfile(MemberDto member, long sellingArticleCount, long likedArticleCount, long offerCount, long reviewCount) {
        this.member = member;
        this.sellingArticleCount = sellingArticleCount;
        this.likedArticleCount = likedArticleCount;
        this.offerCount = offerCount;
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
        return "MyProfile{" +
                "member=" + member +
                ", sellingArticleCount=" + sellingArticleCount +
                ", likedArticleCount=" + likedArticleCount +
                ", offerCount=" + offerCount +
                ", reviewCount=" + reviewCount +
                '}';
    }
}
