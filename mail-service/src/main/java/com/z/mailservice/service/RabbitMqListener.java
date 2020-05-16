package com.z.mailservice.service;


import com.rabbitmq.client.Channel;
import com.z.mailservice.transformer.UserConfirmationTokenTransformer;
import com.z.zcoreblocking.dto.queue.UserConfirmationToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMqListener {
    private final EmailService emailService;
    private final UserConfirmationTokenTransformer userConfirmationTokenTransformer;

    @RabbitListener(queues = "${rabbitmq.queue}", ackMode = "MANUAL")
    public void onNewUser(UserConfirmationToken userConfirmationToken, Channel channel,
                          @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        log.debug("Received message: {}", userConfirmationToken.toString());
        emailService.sendUserTokenConfirmation(userConfirmationTokenTransformer.transform(userConfirmationToken));
        channel.basicAck(tag, false);
    }
}
