package com.mindaces.mindaces.controller;

import com.mindaces.mindaces.dto.BoardDto;
import com.mindaces.mindaces.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@AllArgsConstructor
public class NotificationController
{
    private final NotificationService notificationService;

    /**
     * @title 작성글의 유저 sse 연결
     */

    @GetMapping(value = "/commentAlarm/setEmitter/{userID}", produces = "text/event-stream")
    public SseEmitter setSSEEmitter(@PathVariable String userID,
                                    @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "")String lastEventId)
    {
        return notificationService.makeMappingSSEEmitter(userID);
    }

    @GetMapping(value = "/commentAlarm/send")
    public void sendAlarm(BoardDto boardDto)
    {
        System.out.println("SEND");
        notificationService.send(boardDto);
    }

}