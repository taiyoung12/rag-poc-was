package com.rag.poc.controller

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
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
        val actual =
            mockMvc.perform(get("/ping"))
                .andExpect(status().isOk)
                .andExpect(content().string("pong"))
                .andReturn()

        assertEquals("pong", actual.response.contentAsString)
    }
}
