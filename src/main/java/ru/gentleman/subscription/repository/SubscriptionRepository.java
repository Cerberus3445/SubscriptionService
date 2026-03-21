package ru.gentleman.subscription.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.gentleman.subscription.entity.Subscription;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {

    Optional<Subscription> findByIdAndIsActive(UUID id, Boolean isActive);

    List<Subscription> findAllByUserIdAndIsActive(UUID userId, Boolean isActive);

    @Query(value = """
                   SELECT * FROM subscription.subscriptions
                   WHERE expiration_date < NOW() AND is_active = true
                   LIMIT 100
                   FOR UPDATE SKIP LOCKED
                   """,
            nativeQuery = true)
    List<Subscription> find100ExpiredSubscriptions();
}
