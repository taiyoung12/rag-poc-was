package com.rag.poc.conifg

import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfig {
    @Bean
    fun ragQueue(): Queue {
        return Queue("rag_queue", false)
    }
}
