package com.sec.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public @Data class CommentDto {
    private Integer postId;
    private Integer memberId;
    private String content;
}
