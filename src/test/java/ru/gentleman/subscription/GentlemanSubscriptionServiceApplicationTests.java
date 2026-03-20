package ru.gentleman.subscription;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class GentlemanSubscriptionServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
