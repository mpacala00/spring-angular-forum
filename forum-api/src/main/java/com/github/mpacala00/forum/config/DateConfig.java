package com.github.mpacala00.forum.config;

import org.joda.time.DateTimeZone;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.mpacala00.forum.service.DateService;
import com.github.mpacala00.forum.service.DateServiceImpl;

@Configuration
public class DateConfig {

    //these had to be moved from DateServiceImpl to seperate file due to forming a cycle
    @Bean
    DateService dateService() {
        return new DateServiceImpl(timeZone());
    }

    @Bean
    DateTimeZone timeZone() {
        //returns the referance time
        return DateTimeZone.UTC;
    }
}
