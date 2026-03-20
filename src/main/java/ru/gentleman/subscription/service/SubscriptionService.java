package ru.gentleman.subscription.service;

import ru.gentleman.subscription.dto.SubscriptionDto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface SubscriptionService {

    SubscriptionDto get(UUID id);

    List<SubscriptionDto> getByUserId(UUID userId);

    SubscriptionDto create(SubscriptionDto subscriptionDto);

    void renew(UUID id, Instant newExpirationDate);

    void cancel(UUID id);

    void expire(UUID id);

    void delete(UUID id);

}
