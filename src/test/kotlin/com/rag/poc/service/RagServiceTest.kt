package com.rag.poc.service

import com.rag.poc.Fixture.RagFixture
import com.rag.poc.rabbitmq.queue.MessageSender
import com.rag.poc.rabbitmq.queue.QueueManagement
import com.rag.poc.rabbitmq.queue.QueueName
import com.rag.poc.util.ExternalApiClient
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
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
    fun `llm model 에게 요청 할 수 있다`() {
        Mockito.`when`(externalApiClient.queryLLM(RagFixture.anyKeyword, RagFixture.anyPrompt)).thenReturn(RagFixture.ragResponseFixture)

        val response = sut.queryLLM(RagFixture.anyKeyword, RagFixture.anyPrompt)

        Mockito.verify(externalApiClient, Mockito.times(1)).queryLLM(RagFixture.anyKeyword, RagFixture.anyPrompt)
        Assertions.assertEquals(RagFixture.ragResponseFixture.data, response)
    }

    @Test
    fun `Message Queue에 메시지를 전송할 수 있다`() {
        val message = RagFixture.messageFixture
        val queue = Mockito.mock(Queue::class.java)
        Mockito.`when`(management.getQueue(QueueName.RAG)).thenReturn(queue)
        Mockito.doNothing().`when`(sender).send(queue, message)

        sut.enqueueRagRequest(RagFixture.anyKeyword, RagFixture.anyPrompt)

        Mockito.verify(management, Mockito.times(1)).getQueue(QueueName.RAG)
        Mockito.verify(sender, Mockito.times(1)).send(queue, message)
    }
}
