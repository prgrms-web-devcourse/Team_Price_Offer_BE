package com.prgrms.offer.domain.member.model.entity;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Optional;

@Service
public class MemberService {

    private final PasswordEncoder passwordEncoder;

    private final MemberRepository MemberRepository;

    public MemberService(PasswordEncoder passwordEncoder, MemberRepository MemberRepository) {
        this.passwordEncoder = passwordEncoder;
        this.MemberRepository = MemberRepository;
    }

    @Transactional(readOnly = true)
    public Member login(String principal, String credentials) {
        Assert.hasText(principal, "principal must be provided.");
        Assert.hasText(credentials, "credentials must be provided.");

        Member member = MemberRepository.findByLoginId(principal)
                .orElseThrow(() -> new UsernameNotFoundException("Could not found user for " + principal));
        member.checkPassword(passwordEncoder, credentials);
        return member;
    }

    @Transactional(readOnly = true)
    public Optional<Member> findByLoginId(String loginId) {
        Assert.hasText(loginId, "loginId must be provided.");
        return MemberRepository.findByLoginId(loginId);
    }

}