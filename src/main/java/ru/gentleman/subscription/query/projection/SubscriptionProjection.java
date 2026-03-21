package ru.gentleman.subscription.query.projection;

import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import ru.gentleman.common.dto.SubscriptionStatus;
import ru.gentleman.common.event.SubscriptionCanceledEvent;
import ru.gentleman.common.event.SubscriptionCreatedEvent;
import ru.gentleman.common.event.SubscriptionExpiredEvent;
import ru.gentleman.common.event.SubscriptionRenewedEvent;
import ru.gentleman.subscription.dto.SubscriptionDto;
import ru.gentleman.subscription.service.SubscriptionService;

@Component
@RequiredArgsConstructor
public class SubscriptionProjection {

    private final SubscriptionService subscriptionService;

    @EventHandler
    public void on(SubscriptionCreatedEvent event) {
        SubscriptionDto subscriptionDto = SubscriptionDto.builder()
                .id(event.id())
                .courseId(event.courseId())
                .createdAt(event.createdAt())
                .expirationDate(event.expirationDate())
                .isActive(event.isActive())
                .status(SubscriptionStatus.ACTIVE)
                .userId(event.userId())
                .build();
        this.subscriptionService.create(subscriptionDto);
    }

    @EventHandler
    public void on(SubscriptionRenewedEvent event) {
        this.subscriptionService.renew(event.id(), event.expirationDate());
    }

    @EventHandler
    public void on(SubscriptionExpiredEvent event) {
        this.subscriptionService.expire(event.id());
    }

    @EventHandler
    public void on(SubscriptionCanceledEvent event) {
        this.subscriptionService.cancel(event.id());
    }
}
