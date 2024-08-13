package com.rag.poc.controller.response

data class RagResponse(
    val code: Int,
    val message: String,
    val data: LLMResponse,
) {
    companion object {
        fun of(response: RagResponse): LLMResponse {
            return LLMResponse(
                response.data.prompt,
                response.data.answer,
            )
        }
    }
}
