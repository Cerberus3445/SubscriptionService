package ru.gentleman.subscription.command.controller;

import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gentleman.subscription.command.CancelSubscriptionCommand;

import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/subscription")
@RequiredArgsConstructor
public class SubscriptionCommandController {

    private final CommandGateway commandGateway;

    private final MessageSource messageSource;

    @PostMapping("/{id}/cancel")
    public ResponseEntity<String> cancel(@PathVariable("id") UUID id) {
        this.commandGateway.sendAndWait(new CancelSubscriptionCommand(id));

        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.subscription.canceled",
                        new Object[]{id},
                        Locale.getDefault()
                )
        );
    }
}
