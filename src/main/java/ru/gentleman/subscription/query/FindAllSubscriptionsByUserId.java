package ru.gentleman.subscription.query;

import java.util.UUID;

public record FindAllSubscriptionsByUserId(
        UUID userId
) {
}
