package ru.gentleman.subscription.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

public record ExpireSubscriptionCommand(
        @TargetAggregateIdentifier
        UUID id
) {
}
