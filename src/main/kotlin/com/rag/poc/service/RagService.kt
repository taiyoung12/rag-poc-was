package com.rag.poc.service

import com.rag.poc.util.ExternalApiClient
import org.springframework.stereotype.Component

@Component
class RagService(
    private val externalApiClient: ExternalApiClient,
) {
    fun queryLLM(): Any  {
        return externalApiClient.queryLLM()
    }
}
