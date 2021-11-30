package com.prgrms.offer.domain.member.model.entity;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

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
        checkArgument(isNotEmpty(principal), "principal must be provided.");
        checkArgument(isNotEmpty(credentials), "credentials must be provided.");

        Member member = MemberRepository.findByLoginId(principal)
                .orElseThrow(() -> new UsernameNotFoundException("Could not found user for " + principal));
        member.checkPassword(passwordEncoder, credentials);
        return member;
    }

    @Transactional(readOnly = true)
    public Optional<Member> findByLoginId(String loginId) {
        checkArgument(isNotEmpty(loginId), "loginId must be provided.");
        return MemberRepository.findByLoginId(loginId);
    }

}