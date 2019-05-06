package com.geekbrains.springbootproject;

import com.geekbrains.springbootproject.receivers.ConfirmationReceiver;
import com.rabbitmq.client.impl.AMQImpl;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBootProjectApplication {

	@Bean
	Queue confirmationQueue(){return new Queue("confirmationQueue",false);}

	@Bean
	DirectExchange confirmationExchange(){return new DirectExchange("confirmationExchange");}

	@Bean
	Binding confirmationBinding(Queue queue, DirectExchange exchange){
		return BindingBuilder.bind(queue).to(exchange).with("ordConfirm");
	}

	@Bean
	MessageListenerAdapter confirmationListener(ConfirmationReceiver receiver){
		return new MessageListenerAdapter(receiver,"receiveConfirmation");
	}

	@Bean
	SimpleMessageListenerContainer confirmationContainer(ConnectionFactory connectionFactory,MessageListenerAdapter listener){
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames("confirmationQueue");
		container.setMessageListener(listener);
		return container;
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBootProjectApplication.class, args);
	}
}
