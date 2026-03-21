package ru.gentleman.subscription.config;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.PropagatingErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import ru.gentleman.subscription.command.ExpireSubscriptionCommand;
import ru.gentleman.subscription.command.interceptor.SubscriptionAggregateInterceptor;

@Configuration
public class AxonConfig {

//    @Autowired
//    public void configure(EventProcessingConfigurer config) {
//        config.registerListenerInvocationErrorHandler("quiz-group",
//                conf -> PropagatingErrorHandler.instance());
//        config.registerListenerInvocationErrorHandler("question-group",
//                conf -> PropagatingErrorHandler.instance());
//        config.registerListenerInvocationErrorHandler("quiz-attempt-group",
//                conf -> PropagatingErrorHandler.instance());
//        config.registerListenerInvocationErrorHandler("user-answer-group",
//                conf -> PropagatingErrorHandler.instance());
//    }
//
    @Autowired
    public void registerCustomerCommandInterceptor(ApplicationContext context, CommandGateway commandGateway) {
        commandGateway.registerDispatchInterceptor(context.getBean(SubscriptionAggregateInterceptor.class));
    }
}
