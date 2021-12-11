package com.prgrms.offer.domain.message.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Getter
public class MessageBox {

    private final String productImage;
    private final UserInfo userInfo;
    private final MessagePart messagePart;

    @AllArgsConstructor
    public static class UserInfo {
        private long userId;
        private String userNickName;
        private String userProfileImage;
        private String address;

    }

    @AllArgsConstructor
    public static class MessagePart {
        private String content;
        @JsonFormat(shape = Shape.STRING, pattern = "YYYY-MM-DD HH:MM:SS.sss")
        private LocalDateTime createdDate;
    }

}
