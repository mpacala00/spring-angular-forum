package com.github.mpacala00.forum.service.mail;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import com.github.mpacala00.forum.exception.ActivationEmailException;
import com.github.mpacala00.forum.pojos.NotificationEmail;

@Service
@Slf4j
@AllArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final MailCreatorServiceImpl mailCreatorServiceImpl;

    public void sendMail(NotificationEmail email) throws ActivationEmailException {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("mpacala00@gmail.com");
            messageHelper.setTo(email.getRecipient());
            messageHelper.setSubject(email.getSubject());
            messageHelper.setText(mailCreatorServiceImpl.build(email.getBody()));
        };

        try {
            mailSender.send(messagePreparator);
            log.info("Activation email sent");
        } catch (MailException e) {
            throw new ActivationEmailException("Error while sending message to "+email.getRecipient());
        }
    }
}
