package com.rag.poc.conifg

import com.rag.poc.rabbitmq.queue.QueueName
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMqConfig() {
    @Value("\${rabbitmq.host}")
    private lateinit var host: String

    @Value("\${rabbitmq.port}")
    private var port: Int = 5672

    @Value("\${rabbitmq.username}")
    private lateinit var username: String

    @Value("\${rabbitmq.password}")
    private lateinit var password: String

    @Bean
    fun connectionFactory(): ConnectionFactory {
        val factory = CachingConnectionFactory()
        factory.setHost(host)
        factory.port = port
        factory.username = username
        factory.setPassword(password)
        return factory
    }

    @Bean
    fun rabbitAdmin(connectionFactory: ConnectionFactory): RabbitAdmin {
        return RabbitAdmin(connectionFactory)
    }

    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate {
        val rabbitTemplate = RabbitTemplate(connectionFactory)
        rabbitTemplate.messageConverter = jackson2JsonMessageConverter()
        return rabbitTemplate
    }

    @Bean
    fun jackson2JsonMessageConverter(): Jackson2JsonMessageConverter {
        return Jackson2JsonMessageConverter()
    }

    @Bean
    fun ragQueue(): Queue {
        return Queue(QueueName.RAG.toString(), true)
    }
}
