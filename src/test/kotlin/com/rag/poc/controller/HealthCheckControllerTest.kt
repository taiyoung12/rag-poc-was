package com.rag.poc.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(HealthCheckController::class)
class HealthCheckControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `ping should return pong`() {
        mockMvc.perform(get("/ping"))
            .andExpect(status().isOk)
            .andExpect(content().string("pong"))
            .andDo(
                document(
                    "ping",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                ),
            )
    }
}
