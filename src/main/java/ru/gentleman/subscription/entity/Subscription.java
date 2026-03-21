package ru.gentleman.subscription.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.gentleman.common.dto.SubscriptionStatus;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(schema = "subscription", name = "subscriptions")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Subscription {

    @Id
    private UUID id;

    private UUID userId;

    private UUID courseId;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;

    private Instant expirationDate;

    private Instant createdAt;

    private Boolean isActive;
}
