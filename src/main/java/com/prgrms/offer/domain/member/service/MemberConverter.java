package com.prgrms.offer.domain.member.service;

import com.prgrms.offer.domain.member.model.dto.*;
import com.prgrms.offer.domain.member.model.entity.Member;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MemberConverter {

    public MemberCreateResponse toMemberCreateResponse(Member member) {
        MemberCreateResponse.MemberDto memberDto = MemberCreateResponse.MemberDto.builder()
                .id(member.getId())
                .address(member.getAddress())
                .email(member.getPrincipal())
                .nickname(member.getNickname())
                .build();
        return new MemberCreateResponse(memberDto);
    }

    public MemberResponse toMemberResponse(Member member, String token) {
        MemberResponse.MemberDto memberDto = MemberResponse.MemberDto.builder()
                .id(member.getId())
                .email(member.getPrincipal())
                .token(token)
                .nickname(member.getNickname())
                .offerLevel(member.getOfferLevel())
                .address(member.getAddress())
                .profileImageUrl(member.getProfileImageUrl())
                .build();
        return new MemberResponse(memberDto);
    }

    public MemberProfile toMemberProfile(Member member, long articleCount, long reviewCount) {
        MemberProfile.MemberDto memberDto = MemberProfile.MemberDto.builder()
                .id(member.getId())
                .offerLevel(member.getOfferLevel())
                .nickname(member.getNickname())
                .profileImageUrl(member.getProfileImageUrl())
                .address(member.getAddress())
                .build();
        return new MemberProfile(memberDto, articleCount, reviewCount);
    }

    public MyProfile toMyProfile(Member member, long articleCount, long likeCount, long offerCount, long reviewCount) {
        MyProfile.MemberDto memberDto = MyProfile.MemberDto.builder()
                .id(member.getId())
                .offerLevel(member.getOfferLevel())
                .nickname(member.getNickname())
                .profileImageUrl(member.getProfileImageUrl())
                .address(member.getAddress())
                .build();
        return new MyProfile(memberDto, articleCount, likeCount, offerCount, reviewCount);
    }

    public Member toEntity(MemberCreateRequest request, PasswordEncoder passwordEncoder) {
        return Member.builder()
                .principal(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .address(request.getAddress())
                .nickname(request.getNickname())
                .build();
    }
}
