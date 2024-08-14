package com.rag.poc.Fixture

import com.rag.poc.controller.response.LLMResponse
import com.rag.poc.controller.response.RagResponse
import com.rag.poc.rabbitmq.message.RagMessage

class RagFixture {
    companion object {
        val anyKeyword = "keyword"
        val anyPrompt = "prompt"
        val anyAnswer = "answer"

        val llmResponseFixture =
            LLMResponse(
                prompt = anyPrompt,
                answer = anyAnswer,
            )

        val ragResponseFixture =
            RagResponse(
                code = 200,
                message = "message",
                data = llmResponseFixture,
            )

        val messageFixture =
            RagMessage(
                keyword = anyKeyword,
                prompt = anyPrompt,
            )
    }
}
