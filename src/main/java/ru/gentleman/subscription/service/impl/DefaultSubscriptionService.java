package ru.gentleman.subscription.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gentleman.common.dto.SubscriptionStatus;
import ru.gentleman.common.util.ExceptionUtils;
import ru.gentleman.subscription.cache.CacheClear;
import ru.gentleman.subscription.dto.SubscriptionDto;
import ru.gentleman.subscription.entity.Subscription;
import ru.gentleman.subscription.mapper.SubscriptionMapper;
import ru.gentleman.subscription.repository.SubscriptionRepository;
import ru.gentleman.subscription.service.SubscriptionService;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DefaultSubscriptionService implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    private final SubscriptionMapper subscriptionMapper;

    private final CacheClear cacheClear;

    @Override
    @Cacheable(value = "subscription", key = "#id")
    public SubscriptionDto get(UUID id) {
        log.info("get {}", id );

        Subscription subscription = this.subscriptionRepository.findByIdAndIsActive(id,true)
                .orElse(null);

        return this.subscriptionMapper.toDto(subscription);
    }

    @Override
    @Cacheable(value = "allSubscriptionsByUserId", key = "#userId")
    public List<SubscriptionDto> getByUserId(UUID userId) {
        log.info("getByUserId {}", userId);

        return this.subscriptionMapper.toDto(
                this.subscriptionRepository.findAllByUserIdAndIsActive(userId, true)
        );
    }

    @Override
    @Transactional
    @CacheEvict(value = "allSubscriptionsByUserId", key = "#subscriptionDto.userId()")
    public SubscriptionDto create(SubscriptionDto subscriptionDto) {
        log.info("create {}", subscriptionDto);

        Subscription mappedSubscription = this.subscriptionMapper.toEntity(subscriptionDto);
        mappedSubscription.setIsActive(true);

        Subscription createdSubscription = this.subscriptionRepository.save(mappedSubscription);

        return this.subscriptionMapper.toDto(createdSubscription);
    }

    @Override
    @Transactional
    @CacheEvict(value = "subscription", key = "#id")
    public void renew(UUID id, Instant newExpirationDate) {
        log.info("renew {} {}", id, newExpirationDate);

        this.subscriptionRepository.findByIdAndIsActive(id, true).ifPresentOrElse(subscription -> {
            subscription.setExpirationDate(newExpirationDate);

            this.cacheClear.clearAllSubscriptionsByUserId(subscription.getUserId());
        }, () -> {
            throw ExceptionUtils.notFound("error.subscription.not_found", id);
        });
    }

    @Override
    @Transactional
    @CacheEvict(value = "subscription", key = "#id")
    public void cancel(UUID id) {
        log.info("cancel {}", id);

        this.subscriptionRepository.findByIdAndIsActive(id, true).ifPresentOrElse(subscription -> {
            subscription.setStatus(SubscriptionStatus.CANCELLED);

            this.cacheClear.clearAllSubscriptionsByUserId(subscription.getUserId());
        }, () -> {
            throw ExceptionUtils.notFound("error.subscription.not_found", id);
        });
    }

    @Override
    @Transactional
    @CacheEvict(value = "subscription", key = "#id")
    public void expire(UUID id) {
        log.info("expire {}", id);

        this.subscriptionRepository.findByIdAndIsActive(id, true).ifPresentOrElse(subscription -> {
            subscription.setStatus(SubscriptionStatus.EXPIRED);

            this.cacheClear.clearAllSubscriptionsByUserId(subscription.getUserId());
        }, () -> {
            throw ExceptionUtils.notFound("error.subscription.not_found", id);
        });
    }

    @Override
    @Transactional
    @CacheEvict(value = "subscription", key = "#id")
    public void delete(UUID id) {
        log.info("delete {}", id);

        Subscription subscription = this.subscriptionRepository.findByIdAndIsActive(id, true)
                .orElseThrow(() -> ExceptionUtils.notFound("error.subscription.not_found", id));

        subscription.setIsActive(false);
        this.cacheClear.clearAllSubscriptionsByUserId(subscription.getUserId());
    }

    @Override
    public List<SubscriptionDto> getExpiredSubscriptions() {
        log.info("getExpiredSubscriptions");

        return this.subscriptionMapper.toDto(
                this.subscriptionRepository.find100ExpiredSubscriptions()
        );
    }

    @Override
    public boolean existsById(UUID id) {
        log.info("existsById {}", id);

        return this.subscriptionRepository.existsById(id);
    }
}
