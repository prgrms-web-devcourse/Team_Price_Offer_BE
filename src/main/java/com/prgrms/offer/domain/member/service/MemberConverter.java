package com.prgrms.offer.domain.member.service;

import com.prgrms.offer.domain.member.model.dto.MemberCreateRequest;
import com.prgrms.offer.domain.member.model.dto.MemberCreateResponse;
import com.prgrms.offer.domain.member.model.dto.MemberResponse;
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
                .appleLevel(member.getAppleLevel())
                .address(member.getAddress())
                .profileImage(member.getProfileImage())
                .build();
        return new MemberResponse(memberDto);
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
