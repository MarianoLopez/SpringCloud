package com.z.mailservice.service;

import com.z.zcoreblocking.dto.queue.UserConfirmationToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final RetryTemplate retryTemplate;
    private final JavaMailSender javaMailSender;
    private final ConfirmNewAccountService confirmNewAccountService;

    private Executor executor = Executors.newCachedThreadPool();

    CompletableFuture<Boolean> sendUserTokenConfirmation(UserConfirmationToken userConfirmationToken) throws MessagingException {
        var message = javaMailSender.createMimeMessage();
        confirmNewAccountService.setHtmlTemplate(message, userConfirmationToken);
        return this.sendMessage(message);
    }


    private CompletableFuture<Boolean> sendMessage(MimeMessage message) throws MessagingException {
        log.debug("Mail (to {}, subject {})", message.getAllRecipients(), message.getSubject());

        return CompletableFuture.supplyAsync(()-> retryTemplate.execute((RetryCallback<Boolean, MailException>) retryContext -> {
            log.debug("Retry info: {}", retryContext.toString());
            javaMailSender.send(message);
            log.debug("Mail sent");
            return true;
        }, arg -> false), executor);
    }
}
