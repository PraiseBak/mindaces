package com.mindaces.mindaces.controller;

import com.mindaces.mindaces.dto.BoardDto;
import com.mindaces.mindaces.service.NotificationService;
import com.mindaces.mindaces.service.RoleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@AllArgsConstructor
public class NotificationController
{
    private final NotificationService notificationService;
    private final RoleService roleService;

    /**
     * @title 작성글의 유저 sse 연결
     */

    @GetMapping(value = "/commentAlarm/setEmitter", produces = "text/event-stream")
    public SseEmitter setSSEEmitter(@RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "")String lastEventId,
                                    Authentication authentication)
    {
        String username = roleService.getUsername(authentication);
        if(lastEventId.length() != 0)
        {
            log.info("lastEventId :  "+ lastEventId);
        }
        if(!username.equals("-"))
        {
            return notificationService.makeMappingSSEEmitter(username);
        }
        return null;
    }

    @GetMapping(value = "/commentAlarm/send")
    public void sendAlarm(BoardDto boardDto)
    {
        notificationService.send(boardDto);
    }

}

//
///*
//@Slf4j @RestController
//@RequestMapping("/api/notification")
//public
//class
//NotificationController {
//    private final NotificationService notificationService;
//    public NotificationController(NotificationService notificationService) {
//        this.notificationService = notificationService;
//    }
//    @GetMapping(value = "user/push", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public ResponseEntity < SseEmitter > fetchNotify(@AuthenticationPrincipal CustomOAuth2User oAuth2User, @RequestParam(required = false)String uuid) {
//        if (oAuth2User == null || uuid == null) {
//            throw
//                    new
//                            UnauthorizedException("식별되지 않은 유저의 요청입니다.");
//        }
//        final
//        SseEmitter
//                emitter = new
//                SseEmitter();
//        final
//        User
//                user = oAuth2User.getUser();
//        final
//        StreamDataSet
//                DATA_SET = new
//                StreamDataSet(user, emitter);
//        final
//        String
//                UNIQUE_UUID = uuid;
//        try {
//            notificationService.addEmitter(UNIQUE_UUID, DATA_SET);
//        } catch (Exception e) {
//            throw
//                    new
//                            InternalServerException(e.getMessage());
//        }
//        emitter.onCompletion(() -> {
//            notificationService.removeEmitter(UNIQUE_UUID);
//        });
//        emitter.onTimeout(() -> {
//            emitter.complete();
//            notificationService.removeEmitter(UNIQUE_UUID);
//        });
//        return
//                new
//                        ResponseEntity <> (emitter, HttpStatus.OK);
//    }
//}
//
//@Slf4j @Service
//@EnableScheduling
//public
//class
//NotificationService { /** 생략 **/
//private final ConcurrentHashMap<String
//        , StreamDataSet> eventMap = new ConcurrentHashMap<>();
//    void addEmitter(final String UNIQUE_UUID, final StreamDataSet dataSet) {
//        eventMap.put(UNIQUE_UUID, dataSet);
//    }
//    void removeEmitter(final String UNIQUE_UUID) {
//        eventMap.remove(UNIQUE_UUID);
//    }
//    @Scheduled(initialDelay = 2000 L, fixedDelay = 5000 L)
//    public void fetch() {
//        if (eventMap.size() == 0) {
//            return;
//        }
//        this.handleAlert();
//    }
//
//    @Slf4j @Service
//    @EnableScheduling
//    public
//    class
//    NotificationService { /** 생략 **/
//    @Transactional public void handleAlert() {
//        List < String > toBeRemoved = new
//                ArrayList <> (eventMap.size());
//        List < Long > alertIdList = new
//                ArrayList <> ();
//        for (Map.Entry<String
//                , StreamDataSet> entry : eventMap.entrySet()) {
//            final
//            String
//                    uniqueKey = entry.getKey();
//            final
//            StreamDataSet
//                    dataSet = entry.getValue();
//            final
//            User
//                    user = dataSet.getUser();
//            final
//            List < Notification > receivingAlert = notificationRepository.findByNotificationTargetUserUidAndIsReadIsFalse(user.getUid());
//            final
//            int
//                    noneReadCount = receivingAlert.size();
//            /** 접속 유저가 읽지 않은 알람의 개수 **/
//            if (noneReadCount == 0) {
//                continue;
//            }
//            final
//            SseEmitter
//                    emitter = dataSet.getSseEmitter();
//            /** 30분 이내에 작성된 알람 목록 확인 **/
//            final
//            List < Notification > alertList = getListAnMinuteAndAlertFalse(receivingAlert);
//            if (alertList.size() == 0) {
//                continue;
//            }
//            /** 알림데이터 생성 **/
//            NotificationAlert
//                    alert = NotificationAlert
//                    .builder()
//                    .uid(user.getUid())
//                    .notificationCount(noneReadCount)
//                    .notifications(alertList)
//                    .build();
//            /** 알림 목록 ID 획득 **/
//            alertIdList.addAll(alertList
//                    .stream()
//                    .map(Notification : : getId)
//                .collect(Collectors.toList()));
//            try { /** 알림 전송 수행 **/
//                emitter.send(alert, MediaType.APPLICATION_JSON_UTF8);
//            } catch (Exception e) {
//                log.error("이미터 센드 시 에러 발생 :: {}", e.getMessage());
//                toBeRemoved.add(uniqueKey);
//            }
//        }
//        // for
//        /** 전송된 알람들 IS_ALERT 'Y' 로 변경 **/
//        updateIsAlert(alertIdList);
//        /** 전송 오류 SseEmitter 제거 **/
//        for (String uuid : toBeRemoved) {
//            eventMap.remove(uuid);
//        }
//    }
//    }
//
//
//
//    /**  * - 30분 이전에 발생된 알람 여부  * - 알람 푸시 수행 여부  *  * @param paramList 현재 접속 사용자에게 존재하는 전체 알림  * @return 현재 시간으로부터 30분 이전에 발생한 알림 목록  */
//    private ArrayList < Notification > getListAnMinuteAndAlertFalse(List < Notification > paramList) {
//        ArrayList < Notification > alertList = new ArrayList <>();
//        LocalDateTime beforeTime = LocalDateTime . now(). minusMinutes(30);
//        for(Notification notification : paramList) {
//            boolean isAlert = notification . isAlert();
//            LocalDateTime createdAt = notification . getCreatedAt();
//            if(createdAt . isBefore(beforeTime) || isAlert) {
//                continue;
//            }
//            // 30 분 이내 알리미 & 안 읽은 알리미
//            alertList . add(notification);
//        }
//        return alertList;
//    }
//
//
//    /**  * - 전송된 알림에 대해서 IS_READ 값을 'Y' 로 변경  *  * @param alertIds 전송된 알림 ID 목록  */
//    private
//    void
//    updateIsAlert(List < Long > alertIds) {
//        Set < Long > idSet = new
//                HashSet <> (alertIds);
//        idSet.stream().forEach(notificationRepository : : updateNotificationIsAlertById);
//    }
//
//
//