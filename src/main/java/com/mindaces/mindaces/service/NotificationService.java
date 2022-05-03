package com.mindaces.mindaces.service;

import com.mindaces.mindaces.dto.BoardDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.management.Notification;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
@Builder
@AllArgsConstructor
public class NotificationService {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private HashMap<String,SseEmitter> emitterSet = new HashMap<>();

    public void send(BoardDto boardDto)
    {
        String userID = boardDto.getUser();
        SseEmitter sseEmitter = emitterSet.get(userID);
        sendToClient(sseEmitter,userID,boardDto);
        System.out.println("REAL");
    }

    public SseEmitter makeMappingSSEEmitter(String userId) {
        // 2
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        System.out.println("*\n*\n*\n");
        emitterSet.put(userId,emitter);
        emitter.onCompletion(() -> emitterSet.remove(userId));
        emitter.onTimeout(() -> emitterSet.remove(userId));
        // 503 에러를 방지하기 위한 더미 이벤트 전송
        sendToClient(emitter, userId, "[" + userId + " SSE CREATED]");

        // 4
        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        // 현재는 미수신한 Event 처리는 보류
        //TODO
/*
        if (!lastEventId.isEmpty()) {
            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithId(String.valueOf(userId));
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
        }
 */
        return emitter;
    }

    // 3
    private void sendToClient(SseEmitter emitter, String userID,Object data) {
        try {
            System.out.println("STC");
            emitter.send(SseEmitter.event()
                    .name("sse")
                    .data(data));
        } catch (IOException exception) {
            emitterSet.remove(userID);
            throw new RuntimeException("연결 오류!");
        }
    }
}