package com.rag.poc.service

import com.rag.poc.controller.response.LLMResponse
import com.rag.poc.controller.response.RagResponse
import com.rag.poc.util.ExternalApiClient
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class RagService(
    private val externalApiClient: ExternalApiClient,
    private val rabbitTemplate: RabbitTemplate,
    private val ragQueue: Queue,
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

    fun enqueueRagRequest(
        keyword: String,
        prompt: String,
    ) {
        val message = "$keyword|$prompt"
        rabbitTemplate.convertAndSend(ragQueue.name, message)
    }

    fun processRagRequest(message: String): RagResponse {
        val (keyword, prompt) = message.split("|")
        val response = externalApiClient.queryLLM(keyword, prompt)
        return response
    }
}
