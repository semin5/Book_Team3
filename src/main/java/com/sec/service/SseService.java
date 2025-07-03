package com.sec.service;

import com.sec.dto.Notification;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SseService {

    private final Map<Integer, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter connect(int memberId) {
        SseEmitter emitter = new SseEmitter(60 * 1000L);
        emitters.put(memberId, emitter);

        emitter.onCompletion(() -> emitters.remove(memberId));
        emitter.onTimeout(() -> emitters.remove(memberId));

        try {
            emitter.send(SseEmitter.event().name("connect").data("connected"));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }
        return emitter;
    }

    public void sendNotification(int memberId, String content, int postId) {
        SseEmitter emitter = emitters.get(memberId);
        if (emitter != null) {
            try {
                Map<String, Object> notification = new HashMap<>();
                notification.put("content", content);
                notification.put("postId", postId);
                notification.put("createdAt", LocalDateTime.now().toString());

                emitter.send(SseEmitter.event()
                        .name("notification")
                        .data(notification, MediaType.APPLICATION_JSON));
            } catch (IOException e) {
                emitter.completeWithError(e);
                emitters.remove(memberId);
            }
        }
    }
}
