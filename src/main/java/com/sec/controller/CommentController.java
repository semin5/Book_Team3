package com.sec.controller;

import com.sec.dto.CommentDto;
import com.sec.security.CustomOAuth2User;
import com.sec.service.CommentService;
import com.sec.service.PostService;
import com.sec.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final SseService sseService;
    private final PostService postService;

    @PostMapping
    public String createComment(@ModelAttribute CommentDto commentDto,
                                @AuthenticationPrincipal CustomOAuth2User principal) {

        if (principal == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }
        commentDto.setMemberId(principal.getMember().getMemberId());

        commentService.createComment(commentDto);

        int writerId = principal.getMember().getMemberId();
        int postWriterId = postService.findPostWriterId(commentDto.getPostId());
        if (postWriterId != writerId) {
            sseService.sendNotification(postWriterId, "📬 누군가 당신의 게시글에 댓글을 남겼습니다!", commentDto.getPostId());
        }

        return "redirect:/posts/" + commentDto.getPostId();
    }

    @PostMapping("/{id}/delete")
    public String deleteComment(@PathVariable Integer id,
                                @RequestParam Integer postId) {

        if (postId == null) {
            return "redirect:/posts";
        }
        commentService.deleteComment(id);

        return "redirect:/posts/" + postId;
    }

    @PostMapping("/{id}/update")
    public String updateComment(@PathVariable Integer id,
                                @RequestParam String content,
                                @RequestParam Integer postId,
                                @RequestParam String bookTitle,
                                @RequestParam String bookAuthor,
                                @RequestParam(required = false) Integer bookId) {

        if (content == null || content.trim().isEmpty()) {
            return "redirect:/posts/" + postId + "?error=댓글 내용은 비어 있을 수 없습니다";
        }
        commentService.updateComment(id, content, bookTitle, bookAuthor, bookId);

        return "redirect:/posts/" + postId;
    }
    @PostMapping("/{commentId}/adopt")
    public String adoptComment(@PathVariable int commentId, @RequestParam int postId) {

        commentService.adoptComment(commentId, postId);

        return "redirect:/posts/" + postId;
    }

}
