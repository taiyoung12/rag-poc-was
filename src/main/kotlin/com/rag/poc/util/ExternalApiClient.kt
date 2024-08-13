package com.rag.poc.util

import com.rag.poc.controller.response.RagResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "externalApiClient", url = "\${external.api.url}")
interface ExternalApiClient {
    @GetMapping("/rag")
    fun queryLLM(
        @RequestParam("keyword") keyword: String,
        @RequestParam("prompt") prompt: String,
    ): RagResponse
}
