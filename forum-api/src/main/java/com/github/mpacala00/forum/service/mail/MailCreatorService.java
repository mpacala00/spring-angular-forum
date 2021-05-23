package com.github.mpacala00.forum.service.mail;

public interface MailCreatorService {

    /**
     *
     * @param message email's body
     * @return processed message using {@link org.thymeleaf.context.Context}
     */
    String build(String message);
}
