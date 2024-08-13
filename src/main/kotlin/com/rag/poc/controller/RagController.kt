package com.rag.poc.controller

import com.rag.poc.base.response.Response
import com.rag.poc.controller.response.LLMResponse
import com.rag.poc.message.MsgType.*
import com.rag.poc.service.RagService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class RagController(
    private val ragService: RagService,
) {
    @GetMapping("/rag")
    fun queryLLM(
        @RequestParam("keyword", required = true) keyword: String,
        @RequestParam("prompt", required = true) prompt: String,
    ): Response<LLMResponse> {
        val response = ragService.queryLLM(keyword, prompt)
        return Response.create(
            SUCCESS_REQUEST_LLM_MODEL,
            response,
        )
    }
}
