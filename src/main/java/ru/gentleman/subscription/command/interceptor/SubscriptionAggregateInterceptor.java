package ru.gentleman.subscription.command.interceptor;

import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.stereotype.Component;
import ru.gentleman.common.util.ExceptionUtils;
import ru.gentleman.subscription.command.CancelSubscriptionCommand;
import ru.gentleman.subscription.service.SubscriptionService;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.BiFunction;

@Component
@RequiredArgsConstructor
public class SubscriptionAggregateInterceptor implements MessageDispatchInterceptor<CommandMessage<?>>  {

    private final SubscriptionService subscriptionService;

    @Nonnull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(@Nonnull List<? extends CommandMessage<?>> messages) {
        return (index, command) -> {
            if(CancelSubscriptionCommand.class.equals(command.getPayloadType())) {
                CancelSubscriptionCommand cancelSubscriptionCommand = (CancelSubscriptionCommand) command.getPayload();

                if(!this.subscriptionService.existsById(cancelSubscriptionCommand.id())){
                    ExceptionUtils.notFound("error.subscription.not_found", cancelSubscriptionCommand.id());
                }
            }

            return command;
        };
    }
}
