package com.rag.poc.service

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class RagListener(private val ragService: RagService) {
    @RabbitListener(queues = ["rag_queue"])
    fun receiveMessage(message: String) {
        val response = ragService.processRagRequest(message)
        println(response)
    }
}
