package com.github.mpacala00.forum.service.mail;

import com.github.mpacala00.forum.exception.model.ActivationEmailException;
import com.github.mpacala00.forum.pojos.NotificationEmail;

/** JavaMailSender */
public interface MailService {

    /**
     * <p>send an email using {@link org.springframework.mail.javamail.JavaMailSender} with
     * the help of {@link MailCreatorServiceImpl} that will process body using {@link org.thymeleaf.context.Context}
     * from passed in email
     * </p>
     * @param email email to be sent
     * @throws ActivationEmailException if {@link org.springframework.mail.javamail.JavaMailSender} is unable to send email
     */
    void sendMail(NotificationEmail email) throws ActivationEmailException;

}
