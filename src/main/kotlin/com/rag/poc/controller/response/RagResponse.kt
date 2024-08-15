package com.rag.poc.controller.response

import com.rag.poc.rabbitmq.message.RagMessage

data class RagResponse(
    val code: Int,
    val message: String,
    val data: LLMResponse,
) {
    companion object {
        fun handleFailedRequest(message: RagMessage): RagResponse {
            return RagResponse(
                data =
                    LLMResponse(
                        answer = "죄송합니다. 현재 서비스에 문제가 발생했습니다. 나중에 다시 시도해 주세요.",
                        prompt = message.prompt,
                    ),
                code = 503,
                message = "External API request failed",
            )
        }
    }
}
