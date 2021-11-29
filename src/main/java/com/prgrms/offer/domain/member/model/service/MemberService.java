package com.prgrms.offer.domain.member.model.service;

import com.prgrms.offer.domain.member.model.entity.Member;
import com.prgrms.offer.domain.member.model.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;


    public Member login(String username, String credentials) {
        Member member = memberRepository.findByLoginId(username)
            .orElseThrow(
                () -> new UsernameNotFoundException("Cannot find the username = " + username));
        member.checkPassword(passwordEncoder, credentials);
        return member;
    }
}
