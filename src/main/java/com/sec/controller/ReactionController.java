package com.sec.controller;

import com.sec.dto.TargetType;
import com.sec.security.CustomOAuth2User;
import com.sec.service.PostService;
import com.sec.service.ReactionService;
import com.sec.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class ReactionController {

    private final ReactionService reactionService;
    private final PostService postService;
    private final SseService sseService;

    @PostMapping("/{postId}/react")
    public String react(@PathVariable int postId, @RequestParam String type, @AuthenticationPrincipal CustomOAuth2User principal) {

        int memberId = principal.getMember().getMemberId();

        reactionService.react(postId, memberId, TargetType.POST, type);

        int postWriterId = postService.findPostWriterId(postId);
        if (postWriterId != memberId) {
            String emoji = type.equals("LIKE") ? "👍" : "👎";
            String message = type.equals("LIKE")
                    ? "누군가 당신의 게시글을 좋아요 했습니다!"
                    : "누군가 당신의 게시글을 싫어요 했습니다!";
            sseService.sendNotification(postWriterId, emoji + " " + message, postId);
        }

        return "redirect:/posts/" + postId;
    }
}
