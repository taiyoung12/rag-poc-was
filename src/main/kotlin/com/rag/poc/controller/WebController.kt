package com.rag.poc.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class WebController {
    private var llmResponse: String? = null

    @GetMapping("/llm-response")
    fun getLLMResponse(): String {
        return llmResponse ?: "No response received yet"
    }

    fun updateLLMResponse(response: String) {
        llmResponse = response
    }
}
