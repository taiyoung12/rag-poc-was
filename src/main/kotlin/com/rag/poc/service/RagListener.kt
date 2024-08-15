package com.rag.poc.service

import com.rag.poc.controller.WebController
import com.rag.poc.rabbitmq.message.RagMessage
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class RagListener(
    private val ragService: RagService,
    private val webController: WebController,
) {
    @RabbitListener(queues = ["RAG"])
    fun receiveMessage(message: RagMessage) {
        val response = ragService.processRagRequest(message)
        webController.updateLatestResponse(response.data.answer)
    }
}
