package com.sec.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public @Data class PostSearchCondition {
    private String keyword;
    private String tag;
    private Boolean isSolved;
}
