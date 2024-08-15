package com.rag.poc.rabbitmq.queue

import com.rag.poc.message.MsgType
import com.rag.poc.message.RagException
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component

@Component
class MessageSender(private val rabbitTemplate: RabbitTemplate) {
    fun <T> send(
        queue: Queue,
        message: T,
    ) {
        try {
            rabbitTemplate.convertAndSend(
                queue.name,
                message as Any,
            )
        } catch (e: Exception) {
            throw RagException.withType(MsgType.RABBITMQ_SEND_ERROR)
        }
    }
}
