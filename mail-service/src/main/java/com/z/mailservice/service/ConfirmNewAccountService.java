package com.z.mailservice.service;

import com.z.zcoreblocking.dto.queue.UserConfirmationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Locale;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED;

@Service
@RequiredArgsConstructor
public class ConfirmNewAccountService {
    private final SpringTemplateEngine templateEngine;

    @Value("${confirmation.service.url}")
    private String confirmationUrl;

    void setHtmlTemplate(MimeMessage message, UserConfirmationToken userConfirmationToken) throws MessagingException {
        var helper = new MimeMessageHelper(message, MULTIPART_MODE_MIXED_RELATED, UTF_8.name());

        Map<String, Object> model = Map.of("url", buildConfirmationUrl(userConfirmationToken.getToken()));

        var context = new Context(Locale.ENGLISH, model);
        var html = templateEngine.process("email-new-user", context);

        helper.setTo(userConfirmationToken.getEmail());
        helper.setText(html,true);
        helper.setSubject("Confirm new account");
    }

    private String buildConfirmationUrl(String token) {
        return String.format("%s?confirmationToken=%s", confirmationUrl, token.replace("Bearer ", ""));
    }
}
