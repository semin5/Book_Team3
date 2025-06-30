package com.sec.controller;

import com.sec.dto.ReactionType;
import com.sec.dto.TargetType;
import com.sec.entity.Member;
import com.sec.security.CustomOAuth2User;
import com.sec.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class ReactionController {

    private final ReactionService reactionService;

    @PostMapping("/{postId}/react")
    public String react(@PathVariable int postId,
                        @RequestParam String type,
                        @AuthenticationPrincipal CustomOAuth2User principal) {

        int memberId = principal.getMember().getMemberId();
        reactionService.react(postId, memberId, TargetType.POST, type);
        return "redirect:/posts/" + postId;
    }
}
