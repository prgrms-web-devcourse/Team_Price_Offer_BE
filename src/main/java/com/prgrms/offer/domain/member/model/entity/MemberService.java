package com.prgrms.offer.domain.member.model.entity;

import com.prgrms.offer.common.message.ResponseMessage;
import com.prgrms.offer.core.error.exception.BusinessException;
import com.prgrms.offer.domain.member.model.dto.MemberCreateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class MemberService {

    private final PasswordEncoder passwordEncoder;

    private final MemberRepository memberRepository;

    public MemberService(PasswordEncoder passwordEncoder, MemberRepository memberRepository) {
        this.passwordEncoder = passwordEncoder;
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public Member login(String principal, String credentials) {
        Assert.hasText(principal, "principal must be provided.");
        Assert.hasText(credentials, "credentials must be provided.");

        Member member = memberRepository.findByPrincipal(principal)
                .orElseThrow(() -> new UsernameNotFoundException("Could not found user for " + principal));
        member.checkPassword(passwordEncoder, credentials);
        return member;
    }

    @Transactional(readOnly = true)
    public Optional<Member> findByPrincipal(String principal) {
        Assert.hasText(principal, "principal must be provided.");
        return memberRepository.findByPrincipal(principal);
    }

    @Transactional(readOnly = true)
    public Optional<Member> findBy(String principal) {
        Assert.hasText(principal, "principal must be provided.");
        return memberRepository.findByPrincipal(principal);
    }

    public Member createMember(MemberCreateRequest request) {

        Optional<Member> optionalMember = memberRepository.findByPrincipal(request.getEmail());
        if (optionalMember.isPresent()) {
            throw new BusinessException(ResponseMessage.MEMBER_ALREADY_EXIST);
        }


        Member member = Member.builder()
                .principal(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .address(request.getAddress())
                .nickname(request.getNickname())
                .build();

        return memberRepository.save(member);
    }

    public Member join(OAuth2User oAuth2User, String registrationId) {

        if (oAuth2User == null) {
            throw new IllegalArgumentException("oauth2User must be provided.");
        }
        Assert.hasText(registrationId, "authorizedClientRegistrationId must be provided.");

        String providerId = oAuth2User.getName();
        return findByProviderAndProviderId(registrationId, providerId)
                .orElseGet(() -> {
                    Map<String, Object> attributes = oAuth2User.getAttributes();
                    @SuppressWarnings("unchecked")
                    Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
                    if (properties == null) {
                        throw new IllegalArgumentException("OAuth2User properties is empty");
                    }

                    String nickname = (String) properties.get("nickname");
                    String profileImage = (String) properties.get("profile_image");
                    return memberRepository.save(
                            Member.builder()
                                    .nickname(nickname)
                                    .appleLevel(1)
                                    .profileImage(profileImage)
                                    .provider(registrationId)
                                    .providerId(providerId)
                                    .build());
                });
    }

    public Optional<Member> findByProviderAndProviderId(String provider, String providerId) {
        return memberRepository.findByProviderAndProviderId(provider, providerId);
    }

    public Optional<Member> findByProviderId(String kakaoId) {
        return memberRepository.findByProviderId(kakaoId);
    }
}