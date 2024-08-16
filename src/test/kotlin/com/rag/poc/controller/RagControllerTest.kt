@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.rag.poc.controller

import com.rag.poc.message.MsgType
import com.rag.poc.service.RagService
import com.rag.poc.util.ExternalApiClient
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@ExtendWith(RestDocumentationExtension::class)
@WebMvcTest(RagController::class)
@ActiveProfiles("test")
class RagControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var ragService: RagService

    @MockBean
    private lateinit var externalApiClient: ExternalApiClient

    @Autowired
    private lateinit var context: WebApplicationContext

    @BeforeEach
    fun setUp(restDocumentationExtension: RestDocumentationContextProvider) {
        this.mockMvc =
            MockMvcBuilders.webAppContextSetup(this.context)
                .apply<DefaultMockMvcBuilder>(
                    MockMvcRestDocumentation.documentationConfiguration(restDocumentationExtension),
                ).build()
    }

    @Test
    fun `keyword 와 prompt 를 받으면 LLM Model 에게 답변을 요청할 수 있다`() {
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
            .andDo(
                document(
                    "rag-get",
                    queryParameters(
                        parameterWithName("keyword").description("검색할 키워드"),
                        parameterWithName("prompt").description("프롬프트 내용"),
                    ),
                    responseFields(
                        fieldWithPath("code").description("응답 코드"),
                        fieldWithPath("message").description("응답 메시지"),
                        fieldWithPath("data").description("응답 데이터 (nullable)"),
                        fieldWithPath("key").description("LLM Model에 요청 후 반환된 키 값"),
                    ),
                ),
            )
    }
}
