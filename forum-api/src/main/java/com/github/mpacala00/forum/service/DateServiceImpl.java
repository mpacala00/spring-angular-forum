package com.github.mpacala00.forum.service;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.stereotype.Service;

import java.util.TimeZone;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementing Joda-Time API for reading time zones
 * and unifying the time across all servers - if they were spread
 * over different time zones
 */
@Service
public class DateServiceImpl implements DateService {

    private final DateTimeZone timeZone;

    public DateServiceImpl(DateTimeZone timeZone) {
        super();
        //this checkNotNull comes from Google Guava, really interesting library
        this.timeZone = checkNotNull(timeZone);

        System.setProperty("user.timezone", timeZone.getID());
        TimeZone.setDefault(timeZone.toTimeZone());
        DateTimeZone.setDefault(timeZone);
    }

    @Override
    public DateTime now() {
        return DateTime.now();
    }
}
