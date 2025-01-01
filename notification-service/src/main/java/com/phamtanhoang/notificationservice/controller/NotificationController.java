package com.phamtanhoang.notificationservice.controller;


import com.phamtanhoang.event.dto.NotificationEvent;
import com.phamtanhoang.notificationservice.dto.request.Recipient;
import com.phamtanhoang.notificationservice.dto.request.SendEmailRequest;
import com.phamtanhoang.notificationservice.service.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class NotificationController {

  EmailService emailService;

  @KafkaListener(topics = "notification-delivery")
  public void listenNotificationDelivery(NotificationEvent message) {
    log.info("Received notification: {}", message);
    emailService.sendEmail(SendEmailRequest.builder()
        .to(Recipient.builder().email(message.getRecipient()).build())
        .subject(message.getSubject())
        .htmlContent(message.getBody())
        .build());
  }
}
