package com.rag.poc.controller.response

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class WebController {
    private var lastResponse: String? = null

    @GetMapping("/latest-response")
    fun getLatestResponse(): String {
        return lastResponse ?: "No response received yet"
    }

    fun updateLatestResponse(response: String) {
        lastResponse = response
    }
}
