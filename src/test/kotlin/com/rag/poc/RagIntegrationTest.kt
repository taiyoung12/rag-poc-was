package com.rag.poc
import com.rag.poc.Fixture.RagFixture.Companion.ragResponseFixture
import com.rag.poc.controller.WebController
import com.rag.poc.message.MsgType
import com.rag.poc.rabbitmq.message.RagMessage
import com.rag.poc.service.RagListener
import com.rag.poc.service.RagService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mockito
import org.mockito.kotlin.verify
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = ["external.api.url=http://localhost:8001/api/v1"])
class RagIntegrationTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var rabbitTemplate: RabbitTemplate

    @MockBean
    private lateinit var ragService: RagService

    @MockBean
    private lateinit var webController: WebController

    @Captor
    private lateinit var messageCaptor: ArgumentCaptor<RagMessage>

    private lateinit var listener: RagListener

    @BeforeEach
    fun setUp() {
        listener = RagListener(ragService, webController)
    }

    @Test
    fun `유저가 keyword, prompt 를 전송하면 전체 시나리오가 정상적으로 처리된다`() {
        val keyword = "마이데이터"
        val prompt = "example prompt"

        mockMvc.perform(
            get("/rag")
                .param("keyword", keyword)
                .param("prompt", prompt),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.code").value(MsgType.SUCCESS_REQUEST_LLM_MODEL.toString()))

        verify(rabbitTemplate).convertAndSend(Mockito.anyString(), Mockito.anyString(), messageCaptor.capture())

        val sentMessage = messageCaptor.value
        assertEquals(keyword, sentMessage.keyword)
        assertEquals(prompt, sentMessage.prompt)

        val responseFuture = CompletableFuture<String>()
        Mockito.`when`(ragService.processRagRequest(sentMessage)).thenAnswer {
            ragResponseFixture
        }

        listener.receiveMessage(sentMessage)

        verify(webController).updateLLMResponse("모델 응답")

        responseFuture.get(5, TimeUnit.SECONDS)

        mockMvc.perform(get("/llm-response"))
            .andExpect(status().isOk)
            .andExpect { result -> assertEquals("모델 응답", result.response.contentAsString) }
    }
}
