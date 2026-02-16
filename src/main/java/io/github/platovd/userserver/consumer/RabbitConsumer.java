package io.github.platovd.userserver.consumer;

import io.github.platovd.userserver.user.UserCreationMessage;
import io.github.platovd.userserver.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitConsumer {
    private final UserService userService;

    @RabbitListener(queues = "${queues.user-creation.name}")
    public void receiveUserCreationMessage(@Valid @Payload UserCreationMessage request) {
        userService.createUserFromMessage(request);
    }
}
