package com.rag.poc

import com.rag.poc.Fixture.RagFixture.Companion.anyKeyword
import com.rag.poc.Fixture.RagFixture.Companion.anyPrompt
import com.rag.poc.Fixture.RagFixture.Companion.messageFixture
import com.rag.poc.Fixture.RagFixture.Companion.ragResponseFixture
import com.rag.poc.controller.WebController
import com.rag.poc.message.MsgType
import com.rag.poc.rabbitmq.queue.QueueName
import com.rag.poc.service.RagListener
import com.rag.poc.service.RagService
import com.rag.poc.util.ExternalApiClient
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.verify
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = ["external.api.url=http://localhost:8001/api/v1"])
@ActiveProfiles("test")
class RagIntegrationTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var rabbitTemplate: RabbitTemplate

    @MockBean
    private lateinit var externalApiClient: ExternalApiClient

    @Autowired
    private lateinit var ragService: RagService

    @Autowired
    private lateinit var webController: WebController

    @Autowired
    private lateinit var listener: RagListener

    private val queue = Queue(QueueName.RAG.name, true)

    @BeforeEach
    fun setUp() {
        listener = RagListener(ragService, webController)
    }

    @Test
    fun `유저가 keyword와 prompt를 전송하면 RabbitMQ에 메시지가 들어가고, 구독자가 이를 처리하며 External API 요청 후 LLM 응답을 업데이트한다`() {
        val keyword = anyKeyword
        val prompt = anyPrompt

        // External API에 요청을 보내고 LLM 응답을 업데이트한다 ( 모킹 )
        Mockito.`when`(externalApiClient.queryLLM(keyword, prompt)).thenAnswer {
            ragResponseFixture
        }

        // 유저가 keyword, prompt로 요청을 보낸다
        mockMvc
            .perform(
                get("/rag")
                    .param("keyword", keyword)
                    .param("prompt", prompt),
            ).andExpect(status().isOk)
            .andExpect(jsonPath("$.code").value(MsgType.SUCCESS_REQUEST_LLM_MODEL.toString()))

        // queue에 메시지를 전송한다
        verify(rabbitTemplate).convertAndSend(queue.name, messageFixture)

        // 구독자가 메시지를 처리한다
        listener.receiveMessage(messageFixture)

        // External API에 요청을 보내고 LLM 응답을 업데이트한다
        verify(externalApiClient).queryLLM(keyword, prompt)

        // web에 전송한다.
        mockMvc
            .perform(get("/llm-response"))
            .andExpect(status().isOk)
            .andExpect { result -> assertEquals(ragResponseFixture.data.answer, result.response.contentAsString) }
    }
}
