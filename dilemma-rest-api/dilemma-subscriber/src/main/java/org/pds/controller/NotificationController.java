package org.pds.controller;

import lombok.extern.slf4j.Slf4j;
import org.pds.model.Event;
import org.pds.model.Notification;
import org.pds.service.NotificationImplService;
import org.pds.service.RabbitMQSubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(NotificationController.PATH)
public class NotificationController {
    public static final String PATH = "/api/v1/notifications";

    private NotificationImplService notificationService;
    private RabbitMQSubscriberService rabbitMQSubscriberService;

    @Autowired
    public void setNotificationService(NotificationImplService notificationService) {
        this.notificationService = notificationService;
    }

    @Autowired
    public void setRabbitMQSubscriberService(RabbitMQSubscriberService rabbitMQSubscriberService) {
        this.rabbitMQSubscriberService = rabbitMQSubscriberService;
    }

    @PostMapping("/send")
    public void subscriber(@RequestBody Event event) {
        log.info("Event redirection was received, creating a notification...");
        rabbitMQSubscriberService.sendMessage(event);
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getAll() {
        List<Notification> notifications = this.notificationService.findAll();
        return ResponseEntity
                .ok(notifications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Notification>> getById(@PathVariable String id) {
        log.info("fetching notification with id {}", id);
        Optional<Notification> notification = this.notificationService.findById(id);
        if (notification.isEmpty()) {
            log.error("notification with id {} not found.", id);
            return ResponseEntity
                    .notFound()
                    .build();
        }
        return ResponseEntity
                .ok(notification);
    }
}
