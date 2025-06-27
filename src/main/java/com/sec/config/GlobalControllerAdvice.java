package com.sec.config;

import com.sec.security.CustomOAuth2User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addCurrentUserId(Model model,
                                 @AuthenticationPrincipal CustomOAuth2User principal) {
        if (principal != null && principal.getMember() != null) {
            model.addAttribute("currentUserId", principal.getMember().getMemberId());
        }
    }
}
