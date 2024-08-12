package com.rag.poc.util

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(value = "llm", url = "localhost:8000")
interface ExternalApiClient {
    @PostMapping("/api/v1/rag")
    fun queryLLM(): Any
}
