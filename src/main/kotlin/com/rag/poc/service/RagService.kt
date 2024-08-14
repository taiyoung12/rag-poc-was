package com.rag.poc.service

import com.rag.poc.controller.response.LLMResponse
import com.rag.poc.controller.response.RagResponse
import com.rag.poc.rabbitmq.message.RagMessage
import com.rag.poc.rabbitmq.queue.MessageSender
import com.rag.poc.rabbitmq.queue.QueueManagement
import com.rag.poc.rabbitmq.queue.QueueName
import com.rag.poc.util.ExternalApiClient
import org.springframework.stereotype.Service

@Service
class RagService(
    private val externalApiClient: ExternalApiClient,
    private val sender: MessageSender,
    private val management: QueueManagement,
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
        val message = RagMessage(keyword, prompt)
        val queue = management.getQueue(QueueName.RAG)
        sender.send(queue, message)
    }

    fun processRagRequest(message: RagMessage): RagResponse {
        val response = externalApiClient.queryLLM(message.keyword, message.prompt)
        return response
    }
}
