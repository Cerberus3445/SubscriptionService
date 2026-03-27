package ru.gentleman.subscription.query;

import java.util.UUID;

public record FindSubscriptionByIdQuery(
        UUID id
) {
}
