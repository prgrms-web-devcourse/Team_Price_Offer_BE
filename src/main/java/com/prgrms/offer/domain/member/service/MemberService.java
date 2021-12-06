package com.prgrms.offer.domain.member.service;

import com.prgrms.offer.common.message.ResponseMessage;
import com.prgrms.offer.core.error.exception.BusinessException;
import com.prgrms.offer.core.jwt.JwtAuthentication;
import com.prgrms.offer.core.jwt.JwtAuthenticationToken;
import com.prgrms.offer.domain.member.model.dto.EmailLoginRequest;
import com.prgrms.offer.domain.member.model.dto.MemberCreateRequest;
import com.prgrms.offer.domain.member.model.dto.MemberCreateResponse;
import com.prgrms.offer.domain.member.model.dto.MemberResponse;
import com.prgrms.offer.domain.member.model.entity.Member;
import com.prgrms.offer.domain.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
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
    private final MemberConverter memberConverter;
    private final AuthenticationManager authenticationManager;


    public MemberService(PasswordEncoder passwordEncoder, MemberRepository memberRepository, MemberConverter memberConverter, AuthenticationManager authenticationManager) {
        this.passwordEncoder = passwordEncoder;
        this.memberRepository = memberRepository;
        this.memberConverter = memberConverter;
        this.authenticationManager = authenticationManager;
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


    public MemberResponse login(EmailLoginRequest request) {
        JwtAuthenticationToken token = new JwtAuthenticationToken(request.getEmail(), request.getPassword());
        Authentication resultToken = authenticationManager.authenticate(token);
        JwtAuthentication authentication = (JwtAuthentication) resultToken.getPrincipal();
        Member member = (Member) resultToken.getDetails();
        return memberConverter.toMemberResponse(member, authentication.token);
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

    public MemberCreateResponse createMember(MemberCreateRequest request) {

        Optional<Member> optionalMember = memberRepository.findByPrincipal(request.getEmail());
        if (optionalMember.isPresent()) {
            throw new BusinessException(ResponseMessage.MEMBER_ALREADY_EXIST);
        }
        Member member = memberConverter.toEntity(request, passwordEncoder);
        Member savedMember = memberRepository.save(member);
        return memberConverter.toMemberCreateResponse(savedMember);
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