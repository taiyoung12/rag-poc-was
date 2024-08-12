package com.rag.poc.controller

import com.rag.poc.service.RagService
import org.springframework.http.ResponseEntity
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
    ): ResponseEntity<Any> {
        val response = ragService.queryLLM(keyword, prompt)
        return ResponseEntity.ok(response)
    }
}
