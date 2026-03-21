package ru.gentleman.subscription.query.handler;

import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;
import ru.gentleman.subscription.dto.SubscriptionDto;
import ru.gentleman.subscription.query.FindAllSubscriptionsByUserIdQuery;
import ru.gentleman.subscription.query.FindSubscriptionByIdQuery;
import ru.gentleman.subscription.service.SubscriptionService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SubscriptionQueryHandler {

    private final SubscriptionService subscriptionService;

    @QueryHandler
    public SubscriptionDto on(FindSubscriptionByIdQuery query) {
        return this.subscriptionService.get(query.id());
    }

    @QueryHandler
    public List<SubscriptionDto> on(FindAllSubscriptionsByUserIdQuery query) {
        return this.subscriptionService.getAllByUserId(query.userId());
    }
}
