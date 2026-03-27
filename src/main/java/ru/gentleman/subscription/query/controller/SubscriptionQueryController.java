package ru.gentleman.subscription.query.controller;

import com.fasterxml.jackson.databind.util.ExceptionUtil;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.*;
import ru.gentleman.common.util.ExceptionUtils;
import ru.gentleman.subscription.dto.SubscriptionDto;
import ru.gentleman.subscription.query.FindAllSubscriptionsByUserIdQuery;
import ru.gentleman.subscription.query.FindSubscriptionByIdQuery;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/subscription")
@RequiredArgsConstructor
public class SubscriptionQueryController {

    private final QueryGateway queryGateway;

    @GetMapping("/{id}")
    public SubscriptionDto get(@PathVariable("id") UUID id) {
        SubscriptionDto subscriptionDto = this.queryGateway.query(
                new FindSubscriptionByIdQuery(id), ResponseTypes.instanceOf(SubscriptionDto.class)).join();

        if(subscriptionDto == null) {
            throw ExceptionUtils.notFound("error.subscription.not_found", id);
        }

        return subscriptionDto;
    }

    @GetMapping(params = "userId")
    public List<Object> getAllByUserId(@RequestParam("id") UUID id) {
        return this.queryGateway.query(
                new FindAllSubscriptionsByUserIdQuery(id), ResponseTypes.multipleInstancesOf(Object.class)).join();
    }
}
