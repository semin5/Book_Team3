package com.sec.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public @Data class MemberDto {

    private int memberId;
    private String nickname;
    private String email;
    private LocalDateTime createdAt;
}
