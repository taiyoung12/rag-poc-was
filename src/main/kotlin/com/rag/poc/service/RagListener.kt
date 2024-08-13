package com.rag.poc.service

import com.rag.poc.controller.response.WebController
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class RagListener(
    private val ragService: RagService,
    private val webController: WebController,
) {
    @RabbitListener(queues = ["rag_queue"])
    fun receiveMessage(message: String) {
        val response = ragService.processRagRequest(message)
        println(response)
        webController.updateLatestResponse(response.data.answer)
    }
}
