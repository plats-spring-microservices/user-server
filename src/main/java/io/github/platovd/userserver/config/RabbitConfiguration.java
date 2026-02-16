package io.github.platovd.userserver.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.messaging.converter.JacksonJsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.validation.Validator;

@Configuration
@RequiredArgsConstructor
public class RabbitConfiguration implements RabbitListenerConfigurer {

    @Value("${queues.user-creation.name}")
    private String queueName;

    @Value("${queues.user-creation.exchange}")
    private String exchangeName;

    @Value("${queues.user-creation.routing-key}")
    private String routingKey;

    private final Validator validator;

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(rabbitMessageHandlerMethodFactory());
    }

    @Bean
    public DefaultMessageHandlerMethodFactory rabbitMessageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setValidator(validator);

        JacksonJsonMessageConverter converter = new JacksonJsonMessageConverter();
        converter.setSerializedPayloadClass(String.class);

        factory.setMessageConverter(converter);
        return factory;
    }

    @Bean
    public org.springframework.amqp.support.converter.MessageConverter amqpMessageConverter() {
        return new org.springframework.amqp.support.converter.JacksonJsonMessageConverter();
    }

    // Физические конфигурации RabbitMQ
    @Bean
    public Queue rabbitQueue() {
        return new Queue(queueName);
    }

    @Bean
    public DirectExchange rabbitExchange() {
        return new DirectExchange(exchangeName, true, false);
    }

    @Bean
    public Binding rabbitBinding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }
}