@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.rag.poc.controller

import com.rag.poc.message.MsgType
import com.rag.poc.service.RagService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(RagController::class)
@ActiveProfiles("test")
class RagControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var ragService: RagService

    @Test
    fun `queryLLM2 should return success response when valid keyword and prompt are provided`() {
        val keyword = "마이데이터"
        val prompt = "example prompt"

        Mockito.doNothing().`when`(ragService).enqueueRagRequest(keyword, prompt)

        mockMvc.perform(
            get("/rag")
                .param("keyword", keyword)
                .param("prompt", prompt)
                .contentType(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.code").value(MsgType.SUCCESS_REQUEST_LLM_MODEL.toString()))
            .andExpect(jsonPath("$.message").value("LLM Model 에 성공적으로 요청을 보냈습니다."))
    }
}
