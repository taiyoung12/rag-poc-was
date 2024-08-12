package com.rag.poc.service

import com.rag.poc.util.ExternalApiClient
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class RagServiceTest {
    private val anyString = "anyString"

    @Mock
    private lateinit var externalApiClient: ExternalApiClient

    private lateinit var sut: RagService

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        sut =
            RagService(externalApiClient)
    }

    @Test
    fun `llm model 에게 요청 할 수 있다`() {
        Mockito.`when`(externalApiClient.queryLLM()).thenReturn(anyString)

        val response = sut.queryLLM()

        Mockito.verify(externalApiClient, Mockito.times(1)).queryLLM()
        Assertions.assertEquals(anyString, response)
    }
}
