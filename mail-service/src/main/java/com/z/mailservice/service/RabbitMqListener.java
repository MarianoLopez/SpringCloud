package com.z.mailservice.service;


import com.rabbitmq.client.Channel;
import com.z.zcoreblocking.dto.queue.UserConfirmationToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMqListener {
    private static final String MANUAL_ACK = "MANUAL";
    private static final Boolean MULTI_MESSAGE = false;

    private final EmailService emailService;


    @RabbitListener(queues = "${rabbitmq.queue}", ackMode = MANUAL_ACK)
    public void onNewUser(UserConfirmationToken userConfirmationToken, Channel channel,
                          @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws MessagingException {
        log.debug("Received message: {}", userConfirmationToken.toString());

        emailService.sendUserTokenConfirmation(userConfirmationToken).thenAccept(result -> {
            log.debug("sendUserTokenConfirmation.thenAccept: {}", result);
            try {
                if (result) {
                    channel.basicAck(tag, MULTI_MESSAGE);
                } else {
                    channel.basicReject(tag, MULTI_MESSAGE);
                }
            } catch (IOException ioe) {
                log.error(ioe.getLocalizedMessage());
            }
        });

        log.debug("onNewUser testing async");
    }

    @RabbitListener(queues = "${rabbitmq.queue}.dlx", ackMode = MANUAL_ACK)
    public void onDeadMessage(Message failedMessage) {
        log.debug("DeadMessage: {}", failedMessage.toString());
    }
}

