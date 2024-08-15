package com.rag.poc.rabbitmq.queue

import com.rag.poc.Fixture.RagFixture
import com.rag.poc.message.RagException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.test.context.SpringRabbitTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest
@ExtendWith(SpringExtension::class)
@SpringRabbitTest
@ActiveProfiles("test")
class MessageSenderTest {
    @Autowired
    private lateinit var rabbitTemplate: RabbitTemplate

    @Autowired
    private lateinit var messageSender: MessageSender

    private val queue = Queue(QueueName.TEST.name, true)

    private var receivedMessage: Any? = null

    @RabbitListener(queues = ["RAG"])
    fun listen(message: Any) {
        this.receivedMessage = message
    }

    @Test
    fun `null 메시지를 발행할 때 예외가 발생한다`() {
        val message: String? = null

        assertThrows<RagException> {
            messageSender.send(queue, message)
        }
    }

    @Test
    fun `TEST Queue 에 메시지를 발행 후 소비할 수 있다`() {
        val message = RagFixture.messageFixture

        messageSender.send(queue, message)

        val received = rabbitTemplate.receiveAndConvert(QueueName.TEST.name, 5000)

        assertThat(received).isEqualTo(message)
    }

    @Test
    fun `다른 큐에 발행된 메시지를 수신하지 못한다`() {
        val message = RagFixture.messageFixture
        val anotherQueue = Queue("ANOTHER_QUEUE", true)

        messageSender.send(anotherQueue, message)

        val receivedFromAnotherQueue = rabbitTemplate.receiveAndConvert(QueueName.TEST.name, 5000)
        assertThat(receivedFromAnotherQueue).isNull()
    }
}
