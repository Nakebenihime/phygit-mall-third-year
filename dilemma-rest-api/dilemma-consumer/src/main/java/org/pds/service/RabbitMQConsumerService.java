package org.pds.service;

import lombok.extern.slf4j.Slf4j;
import org.pds.model.Notification;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitMQConsumerService {

    @RabbitListener(queues = "${messaging.rabbitmq.messaging-queue}")
    public void consumeNotification(final Notification notification) {
        log.info("Received a notification from an advertising board: {}", notification.toString());
//        switch (notification.getTitle()) {
//            case "Recognition made by advertising board":
//                log.info("Received a notification from an advertising board: {}", notification.toString());
//                break;
//            default:
//                log.info("Received a notification from an unauthorized source: {}", notification.toString());
//        }
    }
}
