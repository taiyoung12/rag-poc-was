package com.rag.poc.service

import com.rag.poc.Fixture.RagFixture.Companion.messageFixture
import com.rag.poc.Fixture.RagFixture.Companion.ragResponseFixture
import com.rag.poc.controller.WebController
import com.rag.poc.controller.response.RagResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any

class RagListenerTest {
    @Mock
    private lateinit var ragService: RagService

    @Mock
    private lateinit var webController: WebController

    private lateinit var sut: RagListener

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        sut =
            RagListener(
                ragService,
                webController,
            )
    }

    @Test
    fun `메시지를 수신할 수 있다`() {
        Mockito.`when`(ragService.processRagRequest(messageFixture)).thenReturn(ragResponseFixture)

        sut.receiveMessage(messageFixture)

        Mockito.verify(ragService, Mockito.times(1)).processRagRequest(messageFixture)
    }

    @Test
    fun `메시지 수신 후 web에 response를 전달할 수 있다`() {
        Mockito.`when`(ragService.processRagRequest(messageFixture)).thenReturn(ragResponseFixture)

        sut.receiveMessage(messageFixture)

        Mockito.verify(ragService, Mockito.times(1)).processRagRequest(messageFixture)
        Mockito.verify(webController, Mockito.times(1)).updateLLMResponse(any())
    }

    @Test
    fun `processRagRequest 에서 예외가 터졌다면 web에 handleFailedRequest를 전달할 수 있다`() {
        val failedResponse = RagResponse.handleFailedRequest(messageFixture)

        Mockito.`when`(ragService.processRagRequest(messageFixture)).thenReturn(failedResponse)

        sut.receiveMessage(messageFixture)

        Mockito.verify(webController, Mockito.times(1)).updateLLMResponse(
            failedResponse.data.answer,
        )
    }
}
