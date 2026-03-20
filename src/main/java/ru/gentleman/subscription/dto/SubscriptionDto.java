package ru.gentleman.subscription.dto;

import ru.gentleman.common.dto.SubscriptionStatus;

import java.time.Instant;
import java.util.UUID;

public record SubscriptionDto(
        UUID id,
        UUID userId,
        UUID courseId,
        SubscriptionStatus status,
        Instant startDay,
        Instant expirationDate,
        Instant createdAt
) {
}
