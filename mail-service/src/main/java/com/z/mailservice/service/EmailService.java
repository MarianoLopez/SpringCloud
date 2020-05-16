package com.z.mailservice.service;

import com.z.mailservice.dto.EmailRequest;
import com.z.mailservice.transformer.EmailRequestTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final EmailRequestTransformer emailRequestTransformer;
    private Executor executor = Executors.newCachedThreadPool();

    void sendUserTokenConfirmation(EmailRequest emailRequest) {
        this.sendSimpleMessage(emailRequestTransformer.transform(emailRequest));
    }

    private void sendSimpleMessage(SimpleMailMessage message) {
        executor.execute(() -> {
            try {
                javaMailSender.send(message);
                log.debug("Mail (to {}, subject {}) sent", message.getTo(), message.getSubject());
            } catch (MailException mailException) {
                log.error("Mail Exception: to {}, subject {}, error {}",
                        message.getTo(), message.getSubject(), mailException.getLocalizedMessage());
            }
        });
    }
}
