package ru.gentleman.subscription.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.gentleman.subscription.command.ExpireSubscriptionCommand;
import ru.gentleman.subscription.dto.SubscriptionDto;
import ru.gentleman.subscription.service.SubscriptionService;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionHandler {

    private final SubscriptionService subscriptionService;

    private final CommandGateway commandGateway;

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.HOURS)
    public void handle() {
        log.info("handle expired subscriptions");

        List<SubscriptionDto> expiredSubscriptions = this.subscriptionService.getExpiredSubscriptions();

        for(SubscriptionDto subscriptionDto : expiredSubscriptions) {
            this.commandGateway.send(new ExpireSubscriptionCommand(subscriptionDto.id()));
        }
    }
}
