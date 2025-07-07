package com.sec.config;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributeAdvice {

    private final MapConfig mapConfig;

    @ModelAttribute("kakaoApiKey")
    public String kakaoApiKey() {
        return mapConfig.getKey();
    }
}
