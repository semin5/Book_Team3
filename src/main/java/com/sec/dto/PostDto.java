package com.sec.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public @Data class PostDto {
    private String title;
    private String content;
    private boolean isSolved;
    private Set<TagDto> tags;
}
