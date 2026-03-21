package ru.gentleman.subscription.command.aggregate;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateCreationPolicy;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.CreationPolicy;
import org.axonframework.spring.stereotype.Aggregate;
import ru.gentleman.common.command.ActivateSubscriptionCommand;
import ru.gentleman.common.dto.SubscriptionStatus;
import ru.gentleman.common.event.SubscriptionCanceledEvent;
import ru.gentleman.common.event.SubscriptionCreatedEvent;
import ru.gentleman.common.event.SubscriptionExpiredEvent;
import ru.gentleman.common.event.SubscriptionRenewedEvent;
import ru.gentleman.subscription.command.CancelSubscriptionCommand;
import ru.gentleman.subscription.command.ExpireSubscriptionCommand;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Aggregate
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class SubscriptionAggregate {

    @AggregateIdentifier
    private UUID id;

    private UUID userId;

    private UUID courseId;

    private SubscriptionStatus status;

    private Instant expirationDate;

    private Instant createdAt;

    private Boolean isActive;

    private Set<UUID> processedOrders = new HashSet<>();

    @CommandHandler
    @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING) //если агрегат не существует, создаём его
    public void handle(ActivateSubscriptionCommand command) {
        if(id == null) {
            SubscriptionCreatedEvent event = SubscriptionCreatedEvent.builder()
                    .id(command.id())
                    .courseId(command.courseId())
                    .createdAt(command.createdAt())
                    .expirationDate(Instant.now().plus(Duration.ofDays(command.days())))
                    .isActive(command.isActive())
                    .userId(command.userId())
                    .build();

            AggregateLifecycle.apply(event);
        } else {
            if(processedOrders.contains(command.orderId())) {
                log.warn("OrderID already processed, skipping...");
                return;
            }

            Instant newExpirationDate;

            if(status == SubscriptionStatus.EXPIRED || status == SubscriptionStatus.CANCELLED) {
                newExpirationDate = Instant.now().plus(Duration.ofDays(command.days()));
            } else { // ACTIVE
                newExpirationDate = expirationDate.plus(Duration.ofDays(command.days()));
            }

            SubscriptionRenewedEvent event = new SubscriptionRenewedEvent(command.id(),
                    command.orderId(),
                    newExpirationDate
            );

            AggregateLifecycle.apply(event);
        }
    }

    @EventSourcingHandler
    public void on(SubscriptionCreatedEvent event) {
        this.id = event.id();
        this.courseId = event.courseId();
        this.userId = event.userId();
        this.status = SubscriptionStatus.ACTIVE;
        this.expirationDate = event.expirationDate();
        this.createdAt = event.expirationDate();
        this.isActive = event.isActive();
        this.processedOrders.add(event.orderId());
    }

    @EventSourcingHandler
    public void on(SubscriptionRenewedEvent event) {
        this.expirationDate = event.expirationDate();
        this.processedOrders.add(event.orderId());
    }

    @CommandHandler
    public void handle(CancelSubscriptionCommand command) {
        if(status == SubscriptionStatus.CANCELLED) {
            throw new CommandExecutionException(
                    "error.subscription.already_cancelled",
                    null,
                    command.id()
            );
        }

        SubscriptionCanceledEvent event = new SubscriptionCanceledEvent(id);

        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(SubscriptionCanceledEvent event) {
        this.status = SubscriptionStatus.CANCELLED;
    }

    @CommandHandler
    public void handle(ExpireSubscriptionCommand command) {
        if(status == SubscriptionStatus.EXPIRED) {
            throw new CommandExecutionException(
                    "error.subscription.already_expired",
                    null,
                    command.id()
            );
        }

        SubscriptionExpiredEvent event = new SubscriptionExpiredEvent(id);

        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(SubscriptionExpiredEvent event) {
        this.status = SubscriptionStatus.EXPIRED;
    }
}
