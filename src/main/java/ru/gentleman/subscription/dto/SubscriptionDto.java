package ru.gentleman.subscription.dto;

import lombok.Builder;
import ru.gentleman.common.dto.SubscriptionStatus;

import java.time.Instant;
import java.util.UUID;

@Builder
public record SubscriptionDto(
        UUID id,
        UUID userId,
        UUID courseId,
        SubscriptionStatus status,
        Instant expirationDate,
        Instant createdAt,
        Boolean isActive
) {
}
