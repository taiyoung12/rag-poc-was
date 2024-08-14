package com.rag.poc.rabbitmq.queue

import com.fasterxml.jackson.databind.ObjectMapper
import com.rag.poc.rabbitmq.message.RagMessage
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageProperties
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter

class MessageConvertTest {
    private val objectMapper = ObjectMapper()
    private val messageConverter = Jackson2JsonMessageConverter(objectMapper)

    @Test
    fun `Jackson2JsonMessageConverter 직렬화 와 역직렬화를 할 수 있다`() {
        val originalMessage = RagMessage("keyword", "prompt")

        val messageProperties = MessageProperties()
        val serializedMessage: Message = messageConverter.toMessage(originalMessage, messageProperties)

        val deserializedMessage: RagMessage = messageConverter.fromMessage(serializedMessage) as RagMessage

        assertEquals(originalMessage, deserializedMessage)
    }
}
