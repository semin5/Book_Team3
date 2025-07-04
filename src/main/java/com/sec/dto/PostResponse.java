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
import java.util.concurrent.atomic.AtomicReference;
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
    private String bookTitle;
    private String bookAuthor;

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

        AtomicReference<String> bookTitleRef = new AtomicReference<>(null);
        AtomicReference<String> bookAuthorRef = new AtomicReference<>(null);

        if (Boolean.TRUE.equals(post.getIsSolved())) {
            post.getComments().stream()
                    .filter(c -> Boolean.TRUE.equals(c.getIs_selected()) && c.getBook() != null)
                    .findFirst()
                    .ifPresent(adopted -> {
                        bookTitleRef.set(adopted.getBook().getTitle());
                        bookAuthorRef.set(adopted.getBook().getAuthor());
                    });
        }

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
                .bookTitle(bookTitleRef.get())
                .bookAuthor(bookAuthorRef.get())
                .build();
    }
}
