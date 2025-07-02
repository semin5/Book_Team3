package com.sec.dto;

import com.sec.entity.Map;
import com.sec.entity.Post;
import com.sec.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public @Data class PostResponse {
    private int postId;
    private String content;
    private String title;
    private boolean isSolved;
    private String nickname;
    private Set<String> tags;
    private Set<Integer> tagIds;
    private LocalDateTime createdAt;
    private int viewCnt;
    private int memberId;
    private int likeCount;
    private int dislikeCount;
    private int totalReactionCount;
    private MapRequest mapInfo;

    public static PostResponse from(Post post) {
        return from(post, null);
    }

    public static PostResponse from(Post post, Map map) {
        Set<String> tagNames = post.getTags() == null ? Set.of() :
                post.getTags().stream()
                        .map(Tag::getName)
                        .collect(Collectors.toSet());

        Set<Integer> tagIds = post.getTags() == null ? Set.of() :
                post.getTags().stream()
                        .map(Tag::getTagId)
                        .collect(Collectors.toSet());

        return PostResponse.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .nickname(post.getMember().getNickname())
                .isSolved(Boolean.TRUE.equals(post.getIsSolved()))
                .tags(tagNames)
                .tagIds(tagIds)
                .createdAt(post.getCreatedAt())
                .viewCnt(post.getView_cnt())
                .memberId(post.getMember().getMemberId())
                .mapInfo(map != null ? MapRequest.mapRequest(map) : null)
                .build();
    }
}
