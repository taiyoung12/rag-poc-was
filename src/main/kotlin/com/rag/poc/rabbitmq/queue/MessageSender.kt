package com.rag.poc.rabbitmq.queue

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class MessageSender(private val rabbitTemplate: RabbitTemplate) {
    private val mapper = ObjectMapper()

    fun <T> send(
        queue: Queue,
        message: T,
    ) {
        val jsonMessage = mapper.writeValueAsString(message)

        rabbitTemplate.convertAndSend(
            queue.name,
            jsonMessage,
        )
    }
}
