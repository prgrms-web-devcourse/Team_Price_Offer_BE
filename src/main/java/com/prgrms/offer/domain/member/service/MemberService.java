package com.prgrms.offer.domain.member.service;

import com.prgrms.offer.common.message.ResponseMessage;
import com.prgrms.offer.common.utils.S3ImageUploader;
import com.prgrms.offer.core.error.exception.BusinessException;
import com.prgrms.offer.core.jwt.JwtAuthentication;
import com.prgrms.offer.core.jwt.JwtAuthenticationToken;
import com.prgrms.offer.domain.member.model.dto.*;
import com.prgrms.offer.domain.member.model.entity.Member;
import com.prgrms.offer.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private static final String PROFILE_IMG_DIR = "profileImage";

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final MemberConverter memberConverter;
    private final AuthenticationManager authenticationManager;
    private final S3ImageUploader s3ImageUploader;

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

    // 자체 회원가입
    public MemberCreateResponse createMember(MemberCreateRequest request) {

        Optional<Member> optionalMember = memberRepository.findByPrincipal(request.getEmail());
        if (optionalMember.isPresent()) {
            throw new BusinessException(ResponseMessage.MEMBER_ALREADY_EXIST);
        }

        Member member = Member.builder()
                .nickname(request.getNickname())
                .principal(request.getEmail())
                .address(request.getAddress())
                .appleLevel(1)
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        log.info("member = {}", member);
        Member savedMember = memberRepository.save(member);
        return memberConverter.toMemberCreateResponse(savedMember);
    }

    // 소셜 로그인
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
                                    .principal(providerId)
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

    public boolean isDuplicateEmail(String email) {
        Optional<Member> optionalMember = memberRepository.findByPrincipal(email);
        return optionalMember.isPresent();
    }

    public String getProfileImageUrl(MultipartFile image) throws IOException {
        return s3ImageUploader.upload(image, PROFILE_IMG_DIR);
    }

    public MemberResponse editProfile(JwtAuthentication authentication, ProfileEdit request) {
        Member findMember = memberRepository.findByPrincipal(authentication.loginId).orElseThrow(() -> {
            throw new BusinessException(ResponseMessage.MEMBER_NOT_FOUND);
        });

        findMember.changeNickname(request.getNickname());
        findMember.changeAddress(request.getAddress());
        findMember.changeProfileImage(request.getProfileImageUrl());

        return memberConverter.toMemberResponse(findMember, authentication.token);
    }

    public MemberResponse getProfile(JwtAuthentication authentication) {
        Member findMember = memberRepository.findByPrincipal(authentication.loginId).orElseThrow(() -> {
            throw new BusinessException(ResponseMessage.MEMBER_NOT_FOUND);
        });
        return memberConverter.toMemberResponse(findMember, authentication.token);
    }
}