package com.rag.poc.service

import com.rag.poc.controller.response.LLMResponse
import com.rag.poc.util.ExternalApiClient
import org.springframework.stereotype.Service

@Service
class RagService(
    private val externalApiClient: ExternalApiClient,
) {
    fun queryLLM(
        keyword: String,
        prompt: String,
    ): LLMResponse {
        val ragResponse =
            externalApiClient.queryLLM(
                keyword,
                prompt,
            )
        return ragResponse.data
    }
}
