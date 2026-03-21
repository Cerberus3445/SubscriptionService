package ru.gentleman.subscription.service;

import ru.gentleman.subscription.dto.SubscriptionDto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface SubscriptionService {

    SubscriptionDto get(UUID id);

    List<SubscriptionDto> getAllByUserId(UUID userId);

    SubscriptionDto create(SubscriptionDto subscriptionDto);

    void renew(UUID id, Instant newExpirationDate);

    void cancel(UUID id);

    void expire(UUID id);

    void delete(UUID id);

    List<SubscriptionDto> getExpiredSubscriptions();

    boolean existsById(UUID id);

}
