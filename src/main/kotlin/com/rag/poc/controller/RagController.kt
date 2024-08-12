package com.rag.poc.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RagController {
    @GetMapping("/rag")
    fun queryLLM(): ResponseEntity<String> {
        return ResponseEntity.ok("Rag")
    }
}
