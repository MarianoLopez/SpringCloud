package com.z.mailservice.transformer;

import com.z.mailservice.dto.EmailRequest;
import com.z.zcoreblocking.transformer.Transformer;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class EmailRequestTransformer implements Transformer<EmailRequest, SimpleMailMessage> {
    @Override
    public SimpleMailMessage transform(EmailRequest source) {
        var message = new SimpleMailMessage();

        message.setTo(source.getTo());
        message.setSubject(source.getSubject());
        message.setText(source.getBody());

        return message;
    }
}
