package ru.gentleman.subscription;

import org.springframework.boot.SpringApplication;

public class TestGentlemanSubscriptionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(GentlemanSubscriptionServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
