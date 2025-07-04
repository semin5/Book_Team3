package com.sec.controller;

import com.sec.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final SseService sseService;

    @GetMapping("/notifications/connect")
    public SseEmitter connect(@RequestParam int memberId) {

        return sseService.connect(memberId);
    }
}
