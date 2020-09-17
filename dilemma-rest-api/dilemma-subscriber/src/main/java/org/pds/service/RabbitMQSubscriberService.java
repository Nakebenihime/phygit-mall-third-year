package org.pds.service;

import lombok.extern.slf4j.Slf4j;
import org.pds.configuration.RabbitMQConfiguration;
import org.pds.model.Event;
import org.pds.model.Notification;
import org.pds.repository.NotificationRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
public class RabbitMQSubscriberService {

    private NotificationRepository notificationRepository;
    private RabbitMQConfiguration rabbitMQConfiguration;
    private RabbitTemplate rabbitTemplate;

    public RabbitMQSubscriberService(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Autowired
    public void setNotificationRepository(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Autowired
    public void setRabbitMQConfiguration(RabbitMQConfiguration rabbitMQConfiguration) {
        this.rabbitMQConfiguration = rabbitMQConfiguration;
    }

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(Event event) {
        Notification notification = new Notification("Recognition made by advertising board, detector:" + event.getDetector(), "Welcome to your favorite shopping mall " + event.getUser(), false, new Random().nextInt(50));
        notificationRepository.save(notification);
        log.info("Notification has been saved in database {}", notification.toString());
        rabbitTemplate.convertAndSend(rabbitMQConfiguration.EXCHANGE_NAME, rabbitMQConfiguration.ROUTING_KEY, notification);
        log.info("Notification has been sent to rabbitMQ Topic {}", notification.toString());
    }

}
