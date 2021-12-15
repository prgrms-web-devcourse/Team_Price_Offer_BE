package com.prgrms.offer.domain.member.controller;

import com.prgrms.offer.common.ApiResponse;
import com.prgrms.offer.common.message.ResponseMessage;
import com.prgrms.offer.core.error.exception.BusinessException;
import com.prgrms.offer.core.jwt.JwtAuthentication;
import com.prgrms.offer.domain.member.model.dto.*;
import com.prgrms.offer.domain.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members")
    public ResponseEntity<ApiResponse> checkDuplicateEmail(@RequestParam String email) {
        boolean isDuplicateEmail = memberService.isDuplicateEmail(email);
        if (isDuplicateEmail) {
            return ResponseEntity.ok(ApiResponse.of(ResponseMessage.DUPLICATE_EMAIL));
        }
        return ResponseEntity.ok(ApiResponse.of(ResponseMessage.VALID_EMAIL));
    }

    @PostMapping("/members")
    public ResponseEntity<ApiResponse> createUser(@RequestBody @Valid MemberCreateRequest request) {
        MemberCreateResponse response = memberService.createMember(request);
        return ResponseEntity.ok(
                ApiResponse.of(ResponseMessage.SUCCESS, response)
        );
    }

    @PostMapping("/members/login")
    public ResponseEntity<ApiResponse> emailLogin(@RequestBody @Valid EmailLoginRequest request) {
        MemberResponse response = memberService.login(request);
        return ResponseEntity.ok(
                ApiResponse.of(ResponseMessage.SUCCESS, response)
        );
    }

    @PostMapping("/members/imageUrls")
    public ResponseEntity<ApiResponse> convertToImageUrl(@ModelAttribute MultipartFile image) throws IOException {
        if (image == null || image.isEmpty()) {
            throw new BusinessException(ResponseMessage.INVALID_IMAGE_EXCEPTION);
        }

        Map<String, String> response = new HashMap<>();
        response.put("imageUrl", memberService.getProfileImageUrl(image));
        return ResponseEntity.ok(ApiResponse.of(ResponseMessage.SUCCESS, response));
    }

    @PatchMapping("/members/me")
    public ResponseEntity<ApiResponse> editProfile(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @RequestBody @Valid ProfileEdit request) {
        MemberResponse response = memberService.editProfile(authentication, request);
        return ResponseEntity.ok(ApiResponse.of(ResponseMessage.SUCCESS, response));
    }

    @GetMapping("/members/me")
    public ResponseEntity<ApiResponse> getProfile(@AuthenticationPrincipal JwtAuthentication authentication) {
        MemberResponse response = memberService.getProfile(authentication);
        return ResponseEntity.ok(ApiResponse.of(ResponseMessage.SUCCESS, response));
    }

    @GetMapping("/members/{memberId}")
    public ResponseEntity<ApiResponse> getOthersProfile(@PathVariable Long memberId) {
        MemberProfile response = memberService.getOthersProfile(memberId);
        return ResponseEntity.ok(ApiResponse.of(ResponseMessage.SUCCESS, response));
    }

    @GetMapping("/members/mypage")
    public ResponseEntity<ApiResponse> getMyProfile(@AuthenticationPrincipal JwtAuthentication authentication) {
        MyProfile response = memberService.getMyProfile(authentication);
        return ResponseEntity.ok(ApiResponse.of(ResponseMessage.SUCCESS, response));
    }
}
