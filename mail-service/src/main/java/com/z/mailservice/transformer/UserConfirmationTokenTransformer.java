package com.z.mailservice.transformer;

import com.z.mailservice.dto.EmailRequest;
import com.z.zcoreblocking.dto.queue.UserConfirmationToken;
import com.z.zcoreblocking.transformer.Transformer;
import org.springframework.stereotype.Component;

@Component
public class UserConfirmationTokenTransformer implements Transformer<UserConfirmationToken, EmailRequest> {
    @Override
    public EmailRequest transform(UserConfirmationToken source) {
        String subject = "New account confirmation";
        return new EmailRequest(source.getEmail(), subject, source.getToken());
    }
}
