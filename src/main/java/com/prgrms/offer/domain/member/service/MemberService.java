package com.prgrms.offer.domain.member.service;

import com.prgrms.offer.common.message.ResponseMessage;
import com.prgrms.offer.common.utils.S3ImageUploader;
import com.prgrms.offer.core.config.PropertyProvider;
import com.prgrms.offer.core.error.exception.BusinessException;
import com.prgrms.offer.core.jwt.JwtAuthentication;
import com.prgrms.offer.core.jwt.JwtAuthenticationToken;
import com.prgrms.offer.domain.article.repository.ArticleRepository;
import com.prgrms.offer.domain.article.repository.LikeArticleRepository;
import com.prgrms.offer.domain.member.model.dto.*;
import com.prgrms.offer.domain.member.model.entity.Member;
import com.prgrms.offer.domain.member.repository.MemberRepository;
import com.prgrms.offer.domain.offer.repository.OfferRepository;
import com.prgrms.offer.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final MemberConverter memberConverter;
    private final AuthenticationManager authenticationManager;
    private final S3ImageUploader s3ImageUploader;
    private final ArticleRepository articleRepository;
    private final ReviewRepository reviewRepository;
    private final OfferRepository offerRepository;
    private final LikeArticleRepository likeArticleRepository;

    private final PropertyProvider propertyProvider;

    @Transactional(readOnly = true)
    public Member login(String principal, String credentials) {
        Member member = memberRepository.findByPrincipal(principal)
                .orElseThrow(() -> new BusinessException(ResponseMessage.MEMBER_NOT_FOUND));
        member.checkPassword(passwordEncoder, credentials);
        return member;
    }

    @Transactional(readOnly = true)
    public MemberResponse login(EmailLoginRequest request) {
        JwtAuthenticationToken token = new JwtAuthenticationToken(request.getEmail(), request.getPassword());
        Authentication resultToken = authenticationManager.authenticate(token);
        JwtAuthentication authentication = (JwtAuthentication) resultToken.getPrincipal();
        Member member = (Member) resultToken.getDetails();
        return memberConverter.toMemberResponse(member, authentication.token);
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
                .offerLevel(1)
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
                    String profileImageUrl = (String) properties.get("profile_image");
                    return memberRepository.save(
                            Member.builder()
                                    .principal(providerId)
                                    .nickname(nickname)
                                    .offerLevel(1)
                                    .profileImageUrl(profileImageUrl)
                                    .provider(registrationId)
                                    .providerId(providerId)
                                    .build());
                });
    }

    private Optional<Member> findByProviderAndProviderId(String provider, String providerId) {
        return memberRepository.findByProviderAndProviderId(provider, providerId);
    }

    @Transactional(readOnly = true)
    public boolean isDuplicateEmail(String email) {
        Optional<Member> optionalMember = memberRepository.findByPrincipal(email);
        return optionalMember.isPresent();
    }

    public String getProfileImageUrl(MultipartFile image) throws IOException {
        return s3ImageUploader.upload(image, propertyProvider.getPROFILE_IMG_DIR());
    }

    public MemberResponse editProfile(JwtAuthentication authentication, ProfileEdit request) {
        Member findMember = memberRepository.findByPrincipal(authentication.loginId).orElseThrow(() -> {
            throw new BusinessException(ResponseMessage.MEMBER_NOT_FOUND);
        });

        findMember.changeNickname(request.getNickname());
        findMember.changeAddress(request.getAddress());
        findMember.changeProfileImageUrl(request.getProfileImageUrl());

        return memberConverter.toMemberResponse(findMember, authentication.token);
    }

    @Transactional(readOnly = true)
    public MemberResponse getProfile(JwtAuthentication authentication) {
        Member findMember = memberRepository.findByPrincipal(authentication.loginId).orElseThrow(() -> {
            throw new BusinessException(ResponseMessage.MEMBER_NOT_FOUND);
        });
        return memberConverter.toMemberResponse(findMember, authentication.token);
    }

    @Transactional(readOnly = true)
    public MemberProfile getOthersProfile(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ResponseMessage.MEMBER_NOT_FOUND));
        long sellingArticleCount = articleRepository.countArticlesByWriter(member);
        long reviewCount = reviewRepository.countReviewsByReviewee(member);

        return memberConverter.toMemberProfile(member, sellingArticleCount, reviewCount);
    }

    @Transactional(readOnly = true)
    public MyProfile getMyProfile(JwtAuthentication authentication) {
        Member member = memberRepository.findByPrincipal(authentication.loginId).orElseThrow(() -> {
            throw new BusinessException(ResponseMessage.MEMBER_NOT_FOUND);
        });

        long sellingArticleCount = articleRepository.countArticlesByWriter(member);
        long reviewCount = reviewRepository.countReviewsByReviewee(member);
        long likeArticleCount = likeArticleRepository.countLikeArticlesByMember(member);
        long offerCount = offerRepository.countOffersByOfferer(member);

        return memberConverter.toMyProfile(member, sellingArticleCount, likeArticleCount, offerCount, reviewCount);
    }
}