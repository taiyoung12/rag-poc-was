package com.rag.poc.service

import com.rag.poc.Fixture.RagFixture
import com.rag.poc.Fixture.RagFixture.Companion.messageFixture
import com.rag.poc.controller.response.RagResponse
import com.rag.poc.message.MsgType
import com.rag.poc.message.RagException
import com.rag.poc.rabbitmq.queue.MessageSender
import com.rag.poc.rabbitmq.queue.QueueManagement
import com.rag.poc.rabbitmq.queue.QueueName
import com.rag.poc.util.ExternalApiClient
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.springframework.amqp.AmqpConnectException
import org.springframework.amqp.core.Queue

class RagServiceTest {
    @Mock
    private lateinit var externalApiClient: ExternalApiClient

    @Mock
    private lateinit var sender: MessageSender

    @Mock
    private lateinit var management: QueueManagement

    private lateinit var sut: RagService

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        sut =
            RagService(
                externalApiClient,
                sender,
                management,
            )
    }

    @Test
    fun `Message Queue에 메시지를 전송할 수 있다`() {
        val message = messageFixture
        val queue = Mockito.mock(Queue::class.java)
        Mockito.`when`(management.getQueue(QueueName.RAG)).thenReturn(queue)
        Mockito.doNothing().`when`(sender).send(queue, message)

        sut.enqueueRagRequest(RagFixture.anyKeyword, RagFixture.anyPrompt)

        Mockito.verify(management, Mockito.times(1)).getQueue(QueueName.RAG)
        Mockito.verify(sender, Mockito.times(1)).send(queue, message)
    }

    @Test
    fun `Queue를 가져오지 못한다면 예외처리를 할 수 있다`() {
        Mockito.doThrow(
            RagException.withType(MsgType.DOES_NOT_EXIST_QUEUE),
        ).`when`(management).getQueue(QueueName.RAG)

        assertThrows<RagException> {
            sut.enqueueRagRequest(RagFixture.anyKeyword, RagFixture.anyPrompt)
        }
    }

    @Test
    fun `AmqpConnectException이 발생했을 때 RABBITMQ_CONNECTION_ERROR 예외가 발생한다`() {
        Mockito.doThrow(AmqpConnectException::class.java).`when`(management).getQueue(QueueName.RAG)

        assertThrows<AmqpConnectException> {
            sut.enqueueRagRequest(RagFixture.anyKeyword, RagFixture.anyPrompt)
        }
    }

    @Test
    fun `Message Queue에 메시지를 전송할 때 실패하면 예외처리를 할 수 있다`() {
        val message = messageFixture
        val queue = Mockito.mock(Queue::class.java)
        Mockito.`when`(management.getQueue(QueueName.RAG)).thenReturn(queue)
        Mockito.doThrow(RagException.withType(MsgType.RABBITMQ_SEND_ERROR)).`when`(sender).send(queue, message)

        assertThrows<RagException> {
            sut.enqueueRagRequest(RagFixture.anyKeyword, RagFixture.anyPrompt)
        }

        Mockito.verify(management, Mockito.times(1)).getQueue(QueueName.RAG)
        Mockito.verify(sender, Mockito.times(1)).send(queue, message)
    }

    @Test
    fun `llm model 에게 요청 할 수 있다`() {
        Mockito.`when`(externalApiClient.queryLLM(RagFixture.anyKeyword, RagFixture.anyPrompt)).thenReturn(RagFixture.ragResponseFixture)

        val response = sut.processRagRequest(messageFixture)

        Mockito.verify(externalApiClient, Mockito.times(1)).queryLLM(RagFixture.anyKeyword, RagFixture.anyPrompt)
        Assertions.assertEquals(RagFixture.ragResponseFixture, response)
    }

    @Test
    fun `llm model 서버가 응답하지 않을 때 실패 메시지를 반환 할 수 있다`() {
        Mockito.`when`(externalApiClient.queryLLM(RagFixture.anyKeyword, RagFixture.anyPrompt))
            .thenThrow(RuntimeException("External API Failure"))

        val response = sut.processRagRequest(messageFixture)

        Mockito.verify(externalApiClient, Mockito.times(1)).queryLLM(RagFixture.anyKeyword, RagFixture.anyPrompt)
        Assertions.assertEquals(RagResponse.handleFailedRequest(messageFixture), response)
    }
}
