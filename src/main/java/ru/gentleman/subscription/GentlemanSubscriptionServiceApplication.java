package ru.gentleman.subscription;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@EnableScheduling
@SpringBootApplication
public class GentlemanSubscriptionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GentlemanSubscriptionServiceApplication.class, args);
    }

}
